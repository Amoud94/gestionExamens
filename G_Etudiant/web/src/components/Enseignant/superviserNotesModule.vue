<template>
  <div class="ui container">

    <form class="ui form">
      <div class="fields">
        <div class="four wide field" >
          <label>Module : </label>
          <select v-model="module" @change="check"  class="ui dropdown">
            <option v-for="element in modules" :value="element">{{ element.nom }} </option>
          </select>
        </div>
        <div class="four wide field" v-if="module">
          <label>Filiere : </label>
          <div class="ui disabled input">
            <input   type="text" :value="module.filiere">
          </div>
        </div>
        <div class="four wide field" v-if="module">
          <label>Semester : </label>
          <div class="ui disabled input">
            <input   type="text" :value="module.semester">
          </div>
        </div>

        <div class="four wide field" >
          <label>Session : </label>
          <select v-model="session"  @change="check" class="ui dropdown">
            <option > normal </option>
            <option > rattrapage </option>
          </select>
        </div>
        <div class="four wide field">
          <label>Search : </label>
          <div class="ui category search">
            <div class="ui icon input">
              <i class="search icon"></i>
              <input type="text" v-model="searchQuery" placeholder="Rechercher par nom " />
            </div>
          </div>
        </div>
      </div>
    </form>
    <table class="ui center aligned selectable celled table">
      <thead>
      <tr>
        <th></th>
        <th>cin</th>
        <th>nom</th>
        <th>prenom</th>
        <th>filiere</th>
        <th>semester</th>
        <th>annee scolaire</th>
        <th>NOTE</th>
        <th>REMARQUE</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="row in resultQuery" >
        <td class="collapsing">
          <div class="ui fitted test slider checkbox">
            <input type="checkbox" v-model="row.proved" > <label></label>
          </div>
        </td>
        <td @click="toggleNote(row)">{{row.etudiant.cin }}</td>
        <td @click="toggleNote(row)">{{row.etudiant.nom }}</td>
        <td @click="toggleNote(row)">{{row.etudiant.prenom}}</td>
        <td @click="toggleNote(row)">{{row.filiere}}</td>
        <td @click="toggleNote(row)">{{row.semester}}</td>
        <td @click="toggleNote(row)">{{row.anneeScolaire}}</td>
        <td class="positive" v-if="row.note > 10">{{row.note}} / 20</td>
        <td class="error" v-if="row.note < 10">{{row.note}} / 20</td>
        <td>
          <div class="ui transparent input">
            <input type="text" v-model="row.observation" placeholder="Observation">
          </div>
        </td>
      </tr>
      </tbody>
      <tfoot class="full-width">
      <tr>
        <th></th>
        <th colspan="10">
          <div class="ui right floated small positive button" @click="Send">
            <i class="ui send icon"></i>Envoyer
          </div>
          <div class="ui left floated  small negative check button" v-if="proved" @click="approveAll">
            Désapprouver tout
          </div>
          <div class="ui left floated  small primary check button" v-else @click="approveAll">
            Approver tout
          </div>

        </th>
      </tr>
      </tfoot>
      <div class="ui dimmer">
        <div class="content">
          <h3 class="ui inverted icon header">
            <i class="info icon"></i>
            Vous avez deja valider les notes du module '{{module.nom}}'<br> Session {{session}}
          </h3>
        </div>
      </div>
    </table>
    <div id="SUP_MODAL" class="ui large modal">
      <div class="content">
        <div style="margin: 20px">
          <table class="ui center aligned selectable celled table">
            <thead>
            <tr>
              <th>cin</th>
              <th>nom</th>
              <th>element</th>
              <th>coefficient</th>
              <th>semester</th>
              <th>session</th>
              <th>année scolaire</th>
              <th>NOTE</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="row in grades">
              <td>{{row.etudiant.cin }}</td>
              <td>{{row.etudiant.nom }} {{row.etudiant.prenom}}</td>
              <td>{{row.element.nom}}</td>
              <td>{{row.element.coefficient}}</td>
              <td>{{row.element.module.semester }}</td>
              <td>{{row.session}}</td>
              <td>{{row.element.module.anneeScolaire }}</td>
              <td class="positive" v-if="row.note > 10">{{row.note}} / 20</td>
              <td class="error" v-if="row.note < 10">{{row.note}} / 20</td>
            </tr>
            </tbody>
          </table>

        </div>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "superviserNotesModule",
  data() {
    return {
      modules:[],
      notes:[],
      grades:[],
      current_post:{},
      session:'',
      semester:'',
      module:'',
      proved:'',
      searchQuery:'',
    }
  },
  methods:{
    toggleNote(post){
      this.grades = []
      $('#SUP_MODAL').modal('show')
      this.get_X(post)
    },
    get_X(post){
      this.$SHARED.dispatchAsyncPost(this.$SHARED.services.LISTER_NOTE_X,
          {
            module : post.module,
            filiere : post.filiere,
            nom : post.etudiant.nom,
            prenom : post.etudiant.prenom,
            semester : post.semester,
            anneeScolaire : post.anneeScolaire,
            session : this.session,
          } ).then( data => {
        this.grades = data
      }, () => {})
    },
    getNotes(){
        this.$SHARED.dispatchAsyncPost(this.$SHARED.services.LISTER_NOTES_MODULE,
            {
              Module : this.module.nom,
              session : this.session,
              semester : this.module.semester
            } ).then( data => {
          this.notes = data
        }, () => {})
    },
    approveAll(){
      if(this.proved) {
        $('.test.checkbox').checkbox('uncheck');
        this.proved = false
      }else{
        $('.test.checkbox').checkbox('check');
        this.proved = true
      }
    },
    Send(){
      this.$SHARED.dispatchAsyncPost(this.$SHARED.services.SEND,
          {
            notes : this.notes,
          }).then( data => {
        this.message = data.message;
        this.$notify({
          group: 'crud',
          title: 'Success message',
          type: 'success',
          text: 'les notes ont été validé avec succès !'
        });
        this.proved=false
        this.check()
      }, () => {})
    },
    check(){
      this.$SHARED.dispatchAsyncPost(this.$SHARED.services.CHECK,
          {
            module : this.module.nom,
            filiere: this.module.filiere,
            semester : this.module.semester,
            session : this.session,
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
          this.getNotes()
        }
      }, () => {})
    },
  },
  computed:{
    resultQuery()
    {
      if (this.searchQuery) {
        return this.notes.filter((item) => {
          return this.searchQuery.toLowerCase().split(' ').every(v => item.etudiant.nom.toLowerCase().includes(v))
        })
      } else {
        return this.notes;
      }
    }
  },
  mounted() {
    this.modules = this.$store.state.user.modules
  }
}
</script>

<style scoped>

</style>