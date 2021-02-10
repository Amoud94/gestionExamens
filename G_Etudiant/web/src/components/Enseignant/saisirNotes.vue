<template>
<div class="ui container">

  <form class="ui form">
    <div class="fields">
      <div class="four wide field">
        <label>Element : </label>
        <select v-model="element" @change="getNotes" class="ui dropdown">
          <option v-for="element in elements"  :value="element">{{ element.nom}} </option>
        </select>
      </div>

      <div class="four wide field" v-if="element.module">
        <label>Module : </label>
        <div class="ui disabled input">
          <input  type="text" :value="element.module.nom">
        </div>
      </div>

      <div class="four wide field" v-if="element.module">
        <label>Filiere : </label>
        <div class="ui disabled input">
          <input   type="text" :value="element.module.filiere">
        </div>
      </div>
      <div class="four wide field" v-if="element.module">
        <label>Semester : </label>
        <div class="ui disabled input">
          <input  type="text" :value="element.module.semester">
        </div>
      </div>
      <div class="four wide field" v-if="element.module">
        <label>TYPE SESSION : </label>
        <select v-model="session" @change="getNotes" class="ui dropdown">
          <option > normal </option>
          <option > rattrapage </option>
        </select>
      </div>
    </div>
  </form>
  <table class="ui single line celled table">
    <thead>
    <tr>
      <th> Nom</th>
      <th>Prenom</th>
      <th>Type d'exam</th>
      <th>Note</th>
      <th>Observation</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="row in etudiants" >
      <td>{{ row.nom }}</td>
      <td>{{ row.prenom }}</td>
      <td>
        <form class="ui form">
          <div class="fluid wide field">
          <select v-model="row.typeExam" class="ui dropdown">
            <option > Ecrit </option>
            <option > Oral </option>
          </select>
        </div>
        </form>
      </td>
      <td>
        <div class="ui transparent input">
          <input type="text" v-model="row.note" placeholder="Veuillez saisir la note...">
        </div>
      </td>
      <td>
        <div class="ui transparent input">
          <input type="text" v-model="row.observation" placeholder="Observation">
        </div>
      </td>
    </tr>
    </tbody>
    <tfoot class="full-width">
    <tr>
      <th colspan="10">
        <div class="ui  primary labeled icon button" @click="Ajouter">
          <i class="plus icon" ></i> SAUVEGARDER
        </div>
      </th>
    </tr>
    <tr>
      <th colspan="99">
        <div>
          <pagination :limit="limit" :current_page="page" :count="count" @setPage="setPage" slot="pagination"/>
        </div>
      </th>
    </tr>
    </tfoot>
    <div class="ui closable dimmer">
      <div class="content">
        <h3 class="ui inverted icon header">
          <i class="info icon"></i>
          Vous avez deja saisie les notes d'element '{{element.nom}}'<br> Session {{session}}
        </h3>
      </div>
    </div>
  </table>
  <div class="ui inverted dimmer">
    <div class="ui segment">
      <h3 class="ui inverted icon header" style="color: black">
        <i class="info icon"></i>
        Vous ne pouvez pas accéder aux données<br> vous n'êtes pas responsable de l'enseignement.</h3>
    </div>
  </div>
</div>
</template>

<script>
const pagination = () => import("@/components/pagination.vue")

export default {
  name: "saisirNotes",
  components: {
    pagination
  },

  data(){
    return{
      etudiants:[],
      elements:[],
      notes:[],

      session:'',
      filiere:'',
      dateAjout:'',
      noteRachtage:'',
      element: {},
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
    getNotes(){
        this.$SHARED.dispatchAsyncPost(this.$SHARED.services.LISTER_NOTES,
            {
              enseignant : this.$store.state.user._id,
              filiere : this.element.module.filiere,
              session : this.session,
              semester : this.element.module.semester,
              element : this.element.nom
            } ).then( data => {
          this.notes = data
          if(this.notes.length !== 0){
            $('.table')
                .dimmer('show')
                .dimmer({
                  closable: false
                });
          }else{
            $('.table').dimmer('hide')
            this.getStudents()
          }
        }, () => {})
    },
    Ajouter(){
      let notes = []
      this.etudiants.forEach(e =>{
        notes.push({
          "session":this.session,
          "element":this.element,
          "enseignant":{
            "id":this.$store.state.user._id,
            "nom":this.$store.state.user.nom,
            "prenom":this.$store.state.user.prenom
          },
          "etudiant":{
            "id":e._id,
            "cin":e.cin,
            "nom":e.nom,
            "prenom":e.prenom,
          },
          "note":parseInt(e.note),
          "observation":e.observation,
          "date":Date()
        })
      })
      this.notes.push(...notes)
      this.$SHARED.dispatchAsyncPost(this.$SHARED.services.SAISIRE_NOTE,
          {
            notes : this.notes
          }).then( data => {
        this.message = data.message
        this.notes = []
        this.$notify({
          group: 'crud',
          title: 'Success message',
          type: 'success',
          text: 'les notes ont été saisies avec succès !'
        });
        this.getNotes()
      }, () => {})
    },
    getStudents(){
      let value = this.element.module.filiere
      this.$SHARED.dispatchAsyncPost(this.$SHARED.services.LISTER_STUDENTS_BY_FILIERE,
          {
            filiere : value,
            page: this.page,
            limit: this.limit
          } ).then( data => {
        this.etudiants = data.docs;
        this.count = data.count;
      }, () => {})
    },
  },
  mounted() {
    this.elements = this.$store.state.user.elements
    if(this.elements){
      $('.container').dimmer('hide').dimmer({closable: false});
    }else{
      $('.container').dimmer('show').dimmer({closable: false});
    }
  },
  computed: {

  }
}
</script>

<style scoped>

</style>
