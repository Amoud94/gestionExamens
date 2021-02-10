import Vue from 'vue'
import VueRouter from 'vue-router'
// component
const home = () => import('@/components/login');

const EtudiantDashBoard = () => import('@/components/Etudiant');


const ConsulterNotesEtudiant = () => import('@/components/Etudiant/consulterNotes');
const Messagerie = () => import('@/components/Etudiant/messagerie');
const PoserReclamation = () => import('@/components/Etudiant/poserReclamation');

Vue.use(VueRouter);

const routes = [
	{
		path: '/',
		name: 'home',
		component: home,
	},

	{
		path: '/Etudiant',
		name: 'Etudiant',
		component: EtudiantDashBoard,
		children:[
			{
				path: 'notes',
				name: 'notes',
				component: ConsulterNotesEtudiant,
			},
			{
				path: 'reclamation',
				name: 'reclamation',
				component: PoserReclamation,
			},
			{
				path: 'messagerie',
				name: 'messagerie',
				component: Messagerie,
			}
		]
	},

];

const router = new VueRouter({
	mode: 'history',
	base: process.env.BASE_URL,
	routes
});

export default router
