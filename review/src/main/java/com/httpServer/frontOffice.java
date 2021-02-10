package com.httpServer;

import com.utils.constants.*;
import io.vertx.core.*;
import io.vertx.core.http.*;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.JWTOptions;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.api.contract.RouterFactoryOptions;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.ext.web.handler.*;
import org.apache.log4j.Logger;

public class frontOffice extends AbstractVerticle {

	private JWTAuth JWTAuthProvider;

	private final Logger logger = Logger.getLogger(frontOffice.class);

	private OpenAPI3RouterFactory routerFactory;

	private boolean isProduction;
	private int serverPort;
	private int clientPort;

	@Override
	public void start(Promise<Void> startFuture) {
		try {

			// load the application config file
			JsonObject config = config();

			isProduction = config.getBoolean("isProduction", true);
			serverPort = config.getInteger("serverPort");
			clientPort = config.getInteger("clientPort");

			// start JWT
			JsonObject options = new JsonObject()
					.put("algorithm", "RS256")
					.put("secretKey", config.getJsonObject("jwt").getString("secretKey"))
					.put("publicKey", config.getJsonObject("jwt").getString("publicKey"));

			JWTAuthOptions JWTAuthProviderConfig = new JWTAuthOptions()
					.addPubSecKey(new PubSecKeyOptions(options));

			JWTAuthProvider = JWTAuth.create(vertx, JWTAuthProviderConfig);

			// creating the router routerFactory + start httpServer
			OpenAPI3RouterFactory.create(vertx, "openAPI3.json", ar -> {
				try {
					if (ar.failed()) {
						// Something went wrong during router factory initialization
						logger.error(ar.cause(), ar.cause());
						startFuture.fail(ar.cause());
					} else {

						// Spec loaded with success
						routerFactory = ar.result();

						// serve vue application
						StaticHandler vueProjectHandler = StaticHandler.create()
								.setWebRoot("web/dist/")
								.setIndexPage("index.html")
								.setCachingEnabled(isProduction)
								.setDefaultContentEncoding("UTF-8");

						// add handlers
						routerFactory
								.addHandlerByOperationId("LOGIN", this::Authentification)
								
								.addHandlerByOperationId("GET", vueProjectHandler)
								.addHandlerByOperationId("POST", BodyHandler.create())
								.addHandlerByOperationId("GET", this::globalHandler)
								.addHandlerByOperationId("POST", this::globalHandler)

								.addHandlerByOperationId("privateGET", this::privateControl)
								.addHandlerByOperationId("privatePOST", this::privateControl)
								.addHandlerByOperationId("privateDELETE", this::privateControl)
								.addHandlerByOperationId("privatePUT", this::privateControl)

								.addHandlerByOperationId("GET_NOTE", this::GET_NOTE)
								.addHandlerByOperationId("Lister_Elements_By_Module", this::GetElementsByModule)
								.addHandlerByOperationId("Get_One_By_element", this::GetEnseignantByElement)
								.addHandlerByOperationId("ENVOYER_MESSAGE", this::SAVE_MESSAGE)
								.addHandlerByOperationId("RECEVOIR_MESSAGE", this::GET_MESSAGE)
						;

						//remove CORS
						if(!isProduction)
							routerFactory.addGlobalHandler(
									CorsHandler.create("http://localhost:"+ clientPort)
											.allowedMethod(HttpMethod.GET)
											.allowedMethod(HttpMethod.POST)
											.allowedMethod(HttpMethod.DELETE)
											.allowedMethod(HttpMethod.PUT)
											.allowedMethod(HttpMethod.OPTIONS)
											.allowCredentials(true)
											.allowedHeader("Access-Control-Allow-Method")
											.allowedHeader("Access-Control-Allow-Origin")
											.allowedHeader("Access-Control-Allow-Credentials")
											.allowedHeader("Content-Type")
											.allowedHeader("Content-Encoding")
											.allowedHeader("authorization")
							);

						// generate the router
						Router router = routerFactory.setOptions(new RouterFactoryOptions()).getRouter();

						// create the server
						HttpServerOptions httpServerOptions = new HttpServerOptions()
								.setPort(serverPort)
								.setCompressionSupported(true);

						vertx.createHttpServer(httpServerOptions).requestHandler(router).listen(asyncResult -> {
							if (asyncResult.succeeded()) {
								logger.info("Server is listening : http://localhost:" + serverPort);
								startFuture.complete();
							} else {
								logger.error(asyncResult.cause(), asyncResult.cause());
								startFuture.fail(asyncResult.cause());
							}
						});

					}
				} catch (Exception e) {
					logger.error(e, e);
					startFuture.fail(e);
				}
			});

		} catch (Exception e) {
			logger.error(e, e);
			startFuture.fail(e);
		}
	}

	private void sendResponse(JsonObject responseContent, HttpServerResponse response) {
		try {
			logger.debug("sending http Response, content: " + responseContent+"\n\n");
			response.putHeader("content-type", "application/json; charset=utf-8");
			response.end(responseContent.encode());
		} catch (Exception e) {
			logger.error(e, e);
			JsonObject rs = new JsonObject()
					.put(Fields.RESPONSE_FLUX_SUCCEEDED, false)
					.put(Fields.RESPONSE_FLUX_MESSAGE, Exceptions.TECHNICAL_ERROR.failureCode());
			response.end(rs.encode());
		}
	}
	private void globalHandler(RoutingContext routingContext) {
		logger.debug("Handler `globalHandler`, path: " + routingContext.request().path() + ", body: " + routingContext.getBody());
		routingContext.next();
	}

	private void privateControl(RoutingContext routingContext) {
		try {
			logger.debug("Handler `privateControl`");
			String token = routingContext.request().getHeader(HttpHeaders.AUTHORIZATION);
			JsonObject authInfo = new JsonObject().put("jwt", token);
			logger.debug("authenticate...");
			JWTAuthProvider.authenticate(authInfo, ar -> {
				if (ar.failed()) {
					logger.debug("ko.");
					JsonObject resp = new JsonObject()
							.put(Fields.RESPONSE_FLUX_SUCCEEDED, false)
							.put(Fields.RESPONSE_FLUX_MESSAGE,"failed to authenticate.");
					sendResponse(resp, routingContext.response());
				} else {
					logger.debug("ok.");
					routingContext.next();
				}
			});
		} catch (Exception e) {
			logger.error(e, e);
			JsonObject resp = new JsonObject()
					.put(Fields.RESPONSE_FLUX_SUCCEEDED, false)
					.put(Fields.RESPONSE_FLUX_MESSAGE, Exceptions.TECHNICAL_ERROR.failureCode());
			sendResponse(resp, routingContext.response());
		}
	}
	private void Authentification(RoutingContext routingContext) {
		logger.debug("Handler `Authentification`");

		JsonObject responseContent = new JsonObject();

		JsonObject module = routingContext.getBodyAsJson();

		vertx.eventBus().request("LOGIN", module, ar -> {
			if (ar.failed()){
				logger.error(ar.cause(), ar.cause());
				// todo send http response
			} else {
				JsonObject user = (JsonObject) ar.result().body();
				user.put("permissions",new JsonArray().add("toto"));
				String JWTToken = JWTAuthProvider.generateToken(user, new JWTOptions().setAlgorithm("RS256"));
				responseContent
						.put(Fields.RESPONSE_FLUX_SUCCEEDED, true)
						.put(Fields.RESPONSE_FLUX_DATA, new JsonObject()
								.put("user", user)
								.put("TOKEN", JWTToken)
						);
			}
			sendResponse(responseContent, routingContext.request().response());
		});
	}
	private void AutorisationFILTER(RoutingContext routingContext,String uriConsumer,String permission) {
		String token = routingContext.request().getHeader(HttpHeaders.AUTHORIZATION);
		logger.debug("@@@@ "+ token);
		JsonObject authInfo = new JsonObject().put("jwt", token);
		JWTAuthProvider.authenticate(authInfo, ar -> {
			if (ar.failed()) {
				logger.debug("1");
				JsonObject resp = new JsonObject()
						.put(Fields.RESPONSE_FLUX_SUCCEEDED, false)
						.put(Fields.RESPONSE_FLUX_MESSAGE,"authenticate failed");
				sendResponse(resp, routingContext.response());
			} else {
				User user = ar.result();
				logger.debug("user"+ user.principal());
				user.isAuthorized(permission, res -> {
					if (res.succeeded() && res.result()) {
						JsonObject responseContent = new JsonObject();

						JsonObject module = routingContext.getBodyAsJson();

						vertx.eventBus().request(uriConsumer, module, hdlr -> {
							if (hdlr.failed()){
								responseContent
										.put(Fields.RESPONSE_FLUX_SUCCEEDED, false)
										.put(Fields.RESPONSE_FLUX_MESSAGE,"KO");
							} else {
								responseContent
										.put(Fields.RESPONSE_FLUX_SUCCEEDED, true)
										.put(Fields.RESPONSE_FLUX_DATA, hdlr.result().body());
							}
							sendResponse(responseContent, routingContext.response());
						});
					}else{
						logger.debug("KO");
						JsonObject resp = new JsonObject()
								.put(Fields.RESPONSE_FLUX_SUCCEEDED, false)
								.put(Fields.RESPONSE_FLUX_MESSAGE,"not authorized.");
						sendResponse(resp, routingContext.response());

					}
				});
			}
		});
	}


	private void GET_NOTE(RoutingContext routingContext) {
		logger.debug("Handler `GET CHEFS DEPARTEMENT`");
		AutorisationFILTER(routingContext,"ListerNoteX","toto");
	}

	private void SAVE_MESSAGE(RoutingContext routingContext) {
		logger.debug("Handler `GET CHEFS DEPARTEMENT`");
		AutorisationFILTER(routingContext,"EnvoyerMessage","toto");
	}
	private void GET_MESSAGE(RoutingContext routingContext) {
		logger.debug("Handler `LISTER LES NOTES ENSEIGNANT X ELEMENT X`");
		AutorisationFILTER(routingContext,"ListerMessage","toto");
	}

	private void GetElementsByModule(RoutingContext routingContext) {
		logger.debug("Handler `GET ELEMENT BY FILIERE`");
		AutorisationFILTER(routingContext,"ListerElementsByModule","toto");
	}

	private void GetEnseignantByElement(RoutingContext routingContext) {
		logger.debug("Handler `GET ELEMENT BY FILIERE`");
		AutorisationFILTER(routingContext,"Enseignant_Element","toto");
	}
}
