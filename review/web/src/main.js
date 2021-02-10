import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import shared from './shared'
import Notifications from 'vue-notification'
import VModal from 'vue-js-modal'
import vSuggest from 'v-suggest'


Vue.prototype.$SHARED = shared;
Vue.config.productionTip = false;

Vue.use(Notifications);
Vue.use(vSuggest);

Vue.use(VModal, {dialog: true});

new Vue({
	router,
	store,
	render: h => h(App),
	mounted() {
		if (localStorage.getItem('TOKEN')) {
			this.$store.dispatch("setToken", localStorage.TOKEN)
		}
		if (localStorage.getItem('USER')) {
			const user = localStorage.getItem('USER');
			this.$store.dispatch("setUser", JSON.parse(user))
		}
	},
}).$mount('#app');
