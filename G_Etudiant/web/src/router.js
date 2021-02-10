import Vue from 'vue'
import VueRouter from 'vue-router'
// component
const home = () => import('@/components/home');

const AdminDashboard = () => import('@/components/DashboardAdmin');
const EnseignantDashBoard = () => import('@/components/DashboardEnseignant');

const module = () => import('@/components/Admin/module');
const departements = () => import('@/components/Admin/departements');
const enseignants = () => import('@/components/Admin/enseignants');
const etduiant = () => import('@/components/Admin/etudiants');
const controler = () => import('@/components/Admin/controler');
const messagerie_A = () => import('@/components/Admin/messagerieAdmin');

const Consulteretduiants = () => import('@/components/Enseignant/consulterEtudiants');
const SaisirNotes = () => import('@/components/Enseignant/saisirNotes');
const superviserNotesModule = () => import('@/components/Enseignant/superviserNotesModule');
const ConsulterNotesEnseigant = () => import('@/components/Enseignant/consulterNotes');
const messagerie_E = () => import('@/components/Enseignant/messagerieEns');




Vue.use(VueRouter);

const routes = [
	{
		path: '/',
		name: 'home',
		component: home,
	},
	{
		path: '/EnseignantDashBoard',
		name: 'EnseignantDashBoard',
		component: EnseignantDashBoard,
		children:[
			{
				path: 'saisirNotes',
				name: 'controlesEnseignant',
				component: SaisirNotes
			},
			{
				path: 'consulterNotes',
				name: 'ConsulterNotesEnseignant',
				component: ConsulterNotesEnseigant
			},
			{
				path: 'consulterEtudiants',
				name: 'Consulteretduiants',
				component: Consulteretduiants
			},
			{
				path: 'messagerie',
				name: 'messagerie_E',
				component: messagerie_E
			},
			{
				path: 'Notes_Module',
				name: 'superviserNotesModule',
				component: superviserNotesModule
			}
		]
	},

	{
		path: '/AdminDashboard',
		name: 'AdminDashboard',
		component: AdminDashboard,
		children:[
			{
				path: 'modules',
				name: 'modules',
				component: module,
			},
			{
				path: 'controler',
				name: 'controler',
				component: controler,
			},
			{
				path: 'messagerie',
				name: 'messagerie_A',
				component: messagerie_A
			},
			{
				path: 'departements',
				name: 'departements',
				component: departements
			},
			{
				path: 'enseignants',
				name: 'enseignants',
				component: enseignants
			},
			{
				path: 'etudiants',
				name: 'etduiants',
				component: etduiant
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
