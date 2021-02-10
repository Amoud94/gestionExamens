import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex);

const isProduction = process.env.NODE_ENV === 'production';

if (!isProduction) {
	console.log('DEV');
	// allow ajax cross
	$.ajaxSetup({
		crossDomain: true,
		xhrFields: {
			withCredentials: true
		}
	});
}


const state = {
	loading: false,
	token:'',
	user:''
};

const getters = {};

const mutations = {
	SET_LOADING(state, payload) {
		state.loading = payload
	},
	SET_TOKEN(state, payload) {
		state.token = payload
	},
	SET_USER(state, payload) {
		state.user = payload
	},
};

const actions = {
	setToken(context,payload){
		localStorage.setItem('TOKEN', payload);
		context.commit("SET_TOKEN",payload)
	},
	setUser(context,payload){
		localStorage.setItem('USER', JSON.stringify(payload));
		context.commit("SET_USER",payload)
	}
};

const store = new Vuex.Store({
	state,
	getters,
	mutations,
	actions
});

export default store;
