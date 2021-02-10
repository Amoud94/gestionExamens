import Vue from 'vue'
import store from './store'
let vue = new Vue();


const isProduction = process.env.NODE_ENV === 'production';

const services = {
	LOGIN : '/login',

	greeting: '/greeting',
	addModule : '/addModule',

	AJOUTER_DEPARTEMENT : '/private/AJOUTER_DEPARTEMENT',
	LISTER_DEPARTEMENTS : '/private/LISTER_DEPARTEMENTS',
	SUPPRIMER_DEPARTEMENT : '/private/SUPPRIMER_DEPARTEMENT/',
	MODIFIER_DEPARTEMENT : '/private/MODIFIER_DEPARTEMENT/',

	AJOUTER_FILIERE : '/private/AJOUTER_FILIERE',
	LISTER_FILIERES : '/private/LISTER_FILIERES',
	SUPPRIMER_FILIERE : '/private/SUPPRIMER_FILIERE/',
	MODIFIER_FILIERE : '/private/MODIFIER_FILIERE/',

	AJOUTER_ENSEIGNANT : '/private/AJOUTER_ENSEIGNANT',
	LISTER_ENSEIGNANTS : '/private/LISTER_ENSEIGNANTS',
	SUPPRIMER_ENSEIGNANT : '/private/SUPPRIMER_ENSEIGNANT/',
	MODIFIER_ENSEIGNANT : '/private/MODIFIER_ENSEIGNANT/',

	AJOUTER_ETUDIANT : '/private/AJOUTER_ETUDIANT',
	LISTER_ETUDIANTS : '/private/LISTER_ETUDIANTS',
	SUPPRIMER_ETUDIANT: '/private/SUPPRIMER_ETUDIANT/',
	MODIFIER_ETUDIANT: '/private/MODIFIER_ETUDIANT/',

	AJOUTER_MODULE : '/private/AJOUTER_MODULE',
	LISTER_MODULES : '/private/LISTER_MODULES',
	SUPPRIMER_MODULE: '/private/SUPPRIMER_MODULE/',
	MODIFIER_MODULE: '/private/MODIFIER_MODULE/',

	AJOUTER_ELEMENT : '/private/AJOUTER_ELEMENT',
	LISTER_ELEMENTS : '/private/LISTER_ELEMENTS',
	SUPPRIMER_ELEMENT: '/private/SUPPRIMER_ELEMENT/',
	MODIFIER_ELEMENT: '/private/MODIFIER_ELEMENT/',

	SAISIRE_NOTE : '/private/SAISIE_NOTES',
	LISTER_NOTES : '/private/LISTER_NOTES',
	MODIFIER_NOTE : '/private/MODIFIER_NOTE/',
	SUPPRIMER_NOTE : '/private/SUPPRIMER_NOTE/',

	LISTER_CHEFS_DEPARTEMENT : '/private/CHEFS_DEPARTEMENT',
	LISTER_CHEFS_FILIERE : '/private/CHEFS_FILIERE',

	LISTER_NOTES_MODULE : '/private/LISTER_NOTES_MODULE',

	LISTER_MODULES_BY_NOTES : '/private/LISTER_MODULE_NOTE',

	LISTER_NOTES_ADMIN : '/private/LISTER_NOTES_ADMIN',

	LISTER_ENSEIGNANT_BY_DEPARTEMENT :'/private/ENSEIGNANT_BY_DEPARTEMENT',
	LISTER_MODUlES_BY_FILIERE : '/private/MODULE_BY_FILIERE',
	LISTER_ELEMENTS_BY_MODULE : '/private/ELEMENTS_BY_MODULE',
	LISTER_STUDENTS_BY_FILIERE : '/private/STUDENT_BY_FILIERE',

	LISTER_NOTE_X : '/private/GET_NOTE',

	SEND : '/private/DISTRIBUTOR',
	CHECK : '/private/CHECK_IF_EXIST',

	SEND_MESSAGE: '/private/MESSENGER_SEND',
	RECEIVE_MESSAGE: '/private/MESSENGER_GET',

	NOTES_ETD_CTRL: '/private/NOTES_ETD_CTRL'
};

const messages = {
	// server error codes
	'-1': "TECHNICAL_ERROR", // event bus replay delay
	0: "TECHNICAL_ERROR",
	1: "USER_OR_PASSWORD_INCORRECT",
	2: "DUPLICATED_USERNAME",
	3: "WRONG_PASSWORD",
	4: "SESSION_EXPIRED",
	//
	_200:"fichier charger avec success",
	generic: {
		create_succeeded: "created successfully",
		update_succeeded: "updated successfully",
		delete_succeeded: "removed successfully",
	},
	user: {
		create_succeeded: "user created successfully",
		update_succeeded: "user updated successfully",
		delete_succeeded: "user removed successfully",
	},
	demande: {
		create_succeeded: "demande created successfully",
		update_succeeded: "user updated successfully",
		delete_succeeded: "user removed successfully",
	},
};

const serverAddress = (function () {
	const server_addr_env_dev = window.location.protocol + '//' + window.location.hostname + ":" + 9990;
	return isProduction ? window.location.origin : server_addr_env_dev;
})();

function dispatchAsyncPost(url, data, use_loader = false, notifyOnFailure = true) {
	url = serverAddress + url;
	let headers = {"Content-Encoding": "gzip", "Content-Type": "application/json; charset=utf-8"}
	if(store.state.token){
		headers.authorization = store.state.token
	}
	data = JSON.stringify(data);

	return new Promise((resolve, reject) => {
		// show loader
		use_loader && vue.$store.commit('SET_LOADING', true);
		$.ajax({
			url,
			data,
			headers,
			type: "POST",
			datatype: "json",
			complete: () => {
				// hide loader
				use_loader && vue.$store.commit('SET_LOADING', false);
			}
		}).done(resp => {
			console.log('dispatchAsyncPost\n\turl: %s, \n\tdata: %o\n\tresp: %o', url, data, resp);
			if (resp.succeeded) {
				resolve(resp.data);
			} else {
				if (notifyOnFailure) {
					vue.$notify({
						group: 'user-message',
						type: 'error', // warn , error, success, info
						text: messages[resp.message]
					});
				}
				reject(resp.message);
			}
		}).fail(err => {
			console.error('AjaxFail\n$store.dispatchAsyncPost\n\turl: %s, \n\tdata: %o\n\terr: %o', url, data, err);
			reject(0);
		});
	})
}

function dispatchAsyncPut(url, data, use_loader = false, notifyOnFailure = true) {
	var id = data._id;
	url = serverAddress + url + id;
	let headers = {"Content-Encoding": "gzip", "Content-Type": "application/json; charset=utf-8"}
	if(store.state.token){
		headers.authorization = store.state.token
	}
	data = JSON.stringify(data);

	return new Promise((resolve, reject) => {
		// show loader
		use_loader && vue.$store.commit('SET_LOADING', true);
		$.ajax({
			url,
			data,
			headers,
			type: "PUT",
			datatype: "json",
			complete: () => {
				// hide loader
				use_loader && vue.$store.commit('SET_LOADING', false);
			}
		}).done(resp => {
			console.log('dispatchAsyncPut\n\turl: %s, \n\tdata: %o\n\tresp: %o', url, data, resp);
			if (resp.succeeded) {
				resolve(resp.data);
			} else {
				if (notifyOnFailure) {
					vue.$notify({
						group: 'user-message',
						type: 'error', // warn , error, success, info
						text: messages[resp.message]
					});
				}
				reject(resp.message);
			}
		}).fail(err => {
			console.error('AjaxFail\n$store.dispatchAsyncPut\n\turl: %s, \n\tdata: %o\n\terr: %o', url, data, err);
			reject(0);
		});
	})
}

function dispatchAsyncDelete(url, data, use_loader = false, notifyOnFailure = true) {
	var id =data.id
	url = serverAddress + url + id
	let headers = {"Content-Encoding": "gzip", "Content-Type": "application/json; charset=utf-8"}
	if(store.state.token){
		headers.authorization = store.state.token
	}
	data = JSON.stringify(data)
	return new Promise((resolve, reject) => {
		// show loader
		use_loader && vue.$store.commit('SET_LOADING', true);
		$.ajax({
			url,
			data,
			datatype: "json",
			headers,
			type: "DELETE",
			complete: () => {
				// hide loader
				use_loader && vue.$store.commit('SET_LOADING', false);
			}
		}).done(resp => {
			console.log('dispatchAsyncDelete\n\turl: %s, \n\tdata: %o\n\tresp: %o', url, data, resp);
			if (resp.succeeded) {
				resolve(resp.data);
			} else {
				if (notifyOnFailure) {
					vue.$notify({
						group: 'user-message',
						type: 'error', // warn , error, success, info
						text: messages[resp.message]
					});
				}
				reject(resp.message);
			}
		}).fail(err => {
			console.error('AjaxFail\n$store.dispatchAsyncDelete\n\turl: %s, \n\tdata: %o\n\terr: %o', url, data, err);
			reject(0);
		});
	})
}

function dispatchAsyncGet(url, use_loader = false, notifyOnFailure = true) {
	url = serverAddress + url;
	let headers = {"Content-Encoding": "gzip", "Content-Type": "application/json; charset=utf-8"}
	if(store.state.token){
		headers.authorization = store.state.token
	}
	return new Promise((resolve, reject) => {
		// show loader
		use_loader && vue.$store.commit('SET_LOADING', true);
		$.ajax({
			url,
			type: "GET",
			datatype: "json",
			headers,
			success: (resp => {
				console.log('dispatchAsyncGet\n\turl: %s, \n\tdata: %o\n\tresp: %o', url,  resp);
				if (resp.succeeded) {
					resolve(resp.data);
				} else {
					if (notifyOnFailure) {
						vue.$notify({
							group: 'user-message',
							type: 'error', // warn , error, success, info
							text: messages[resp.message]
						});
					}
					reject(resp.message);
				}

			})
	})
})}


export default {
	serverAddress,
	services,
	messages,
	dispatchAsyncPost,
	dispatchAsyncGet,
	dispatchAsyncPut,
	dispatchAsyncDelete
}
