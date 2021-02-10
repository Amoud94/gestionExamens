<template>
	<div class="ui container">

    <form class="ui form">
      <div class="fields">
        <div class="four wide field">
          <label>Filiere : </label>
          <select v-model="filiere" @change="getStudents" class="ui dropdown">
            <option v-for="filiere in filieres"  :value="filiere">{{ filiere}} </option>
          </select>
        </div>
        <div class="four wide field" v-if="filiere">
          <label>Search : </label>
          <div class="ui category search">
            <div class="ui icon input">
              <i class="search icon"></i>
              <input type="text" v-model="searchQuery" placeholder="Rechercher par nom " />
            </div>
          </div>
        </div>
      </div>
      <div class="fields">
        <div class="fourteen wide field">
          <table class="ui fluid selectable celled table">
            <thead>
            <tr>
              <th>CIN</th>
              <th>nom</th>
              <th>prenom</th>
              <th>Sexe</th>

            </tr>
            </thead>
            <tbody>
            <tr v-for="row in resultQuery" >
              <td @click="toggleView(row)">{{ row.cin }}</td>
              <td>{{ row.nom }}</td>
              <td>{{ row.prenom }}</td>
              <td>{{ row.sexe }}</td>
            </tr>
            </tbody>
            <tfoot class="full-width">
            <tr>
              <th colspan="99">
                <div>
                  <pagination :limit="limit" :current_page="page" :count="count" @setPage="setPage" slot="pagination"/>
                </div>
              </th>
            </tr>
            </tfoot>
          </table>
        </div>
        <div class="ten wide field" >
          <div class="ui raised segments" v-if="doesExist">
            <div class="ui center teal aligned header segment">
              <div class="ui centered card" >
                <a class="image" href="#">
                  <img src="data:image/png;base64,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">
                </a>
              </div>
              <div class="content">
                <h4>Etudiant(e) : {{ etudiant.nom }} {{etudiant.prenom}}</h4>
              </div>
            </div>
            <div class="ui segments">
              <div class="ui teal segment">
                <h5>CIN : {{ etudiant.cin }}</h5>
              </div>
              <div class="ui teal segment">
                <h5>Filiere : {{ etudiant.filiere }}</h5>
              </div>
              <div class="ui teal segment">
                <h5> @ : {{ etudiant.emailPersonnel }}</h5>
              </div>
              <div class="ui teal segment">
                <h5> @ : {{ etudiant.emailInstitutionnel }}</h5>
              </div>
              <div class="ui teal segment">
                <h5>Telephone : {{ etudiant.telephone }}</h5>
              </div>
            </div>
            <div class="ui horizontal segments">
              <div class="ui center aligned teal segment">
                <h5>Sexe : {{ etudiant.sexe }}</h5>
              </div>
              <div class="ui center aligned teal segment">
                <h5>Lieu naissance : {{ etudiant.lieuNaissance }}</h5>
              </div>
            </div>
            <div class="ui horizontal segments">
              <div class="ui center aligned segment">
                <reclamation
                    v-bind:Destinataire="etudiant.nom +' '+ etudiant.prenom"
                    v-bind:Expediteur="this.$store.state.user.nom +' '+ this.$store.state.user.prenom"
                ></reclamation>
              </div>
            </div>
            </div>
        </div>
      </div>
    </form>
    <div class="ui inverted dimmer">
      <div class="content">
        <h3 class="ui inverted icon header" style="color: black">
          <i class="info icon"></i>
          Vous ne pouvez pas accéder aux données<br> vous n'êtes pas responsable de l'enseignement.</h3>
      </div>
    </div>
  </div>
</template>

<script>
const pagination = () => import("@/components/pagination.vue")
const reclamation = () => import("@/components/reclamation.vue")

	export default {
    name: "consulterEtudiants",

    components: {
      pagination,
      reclamation
    },
    data() {
      return {
        elements:[],
        modules:[],
        filieres:[],
        etudiants: [],
        etudiant: {
          nom:'',
          prenom:''
        },
        filiere:'',
        notes:[],
        message: '',
        searchQuery:'',
        searchOption:'',
        page: 1,
        limit: 10,
        count: 0,
      }
    },
    methods: {
      setPage(page){
        this.page = page;
        this.getStudents();
      },
      toggleView(post){
        this.etudiant={}
        this.etudiant=JSON.parse(JSON.stringify(post))
      },

      getStudents(){
        this.$SHARED.dispatchAsyncPost(this.$SHARED.services.LISTER_STUDENTS_BY_FILIERE,
            {
              filiere : this.filiere,
              page: this.page,
              limit: this.limit
            } ).then( data => {
          this.etudiants = data.docs;
          this.count = data.count;
        }, () => {})
      },

      getFiliere(){
        let elements = this.elements
        let modules = this.modules
        let uniqueArray = []
        if(elements){
          elements.forEach(element =>{
            uniqueArray.push( element.module.filiere)
          })
        }else {
          if (modules) {
            modules.forEach(module => {
              uniqueArray.push(module.filiere)
            })
          }
        }
        this.filieres = [...new Set(uniqueArray)]
      }

    },
    computed: {
      doesExist(){
        if(this.filiere && this.etudiant ){
          return true
        }else{
          return false
        }
      },
      check(){

      },
      resultQuery()
      {
        if (this.searchQuery) {
          return this.etudiants.filter((item) => {
            return this.searchQuery.toLowerCase().split(' ').every(v => item.nom.toLowerCase().includes(v))
          })
        } else {
          return this.etudiants;
        }
      }
    },
    mounted() {
      this.modules = this.$store.state.user.modules
      this.elements = this.$store.state.user.elements
      this.getFiliere()
      this.getStudents()
      if(this.filieres.length === 0){
        $('.container').dimmer('show').dimmer({closable: false});
      }
    }


  }
</script>

<style scoped>
</style>
