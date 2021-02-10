<template>
  <div class="ui container">
    <div class="ui category search">
    <form class="ui form">
      <div class="fields">
        <div class="four wide field">
          <label>Filiere</label>
          <select v-model="query.filiere"  @change="getModulesByFiliere"  name="filiere"  class="ui fluid dropdown">
            <option selected disabled >SVP selectionnez une filiere</option>
            <option v-for="filiere in filieres" :value="filiere.nom">{{ filiere.nom}}</option>
          </select>
        </div>
        <div class="four wide field" >
          <label>Semester : </label>
          <select v-model="query.semester" @change="getModulesByFiliere"  class="ui dropdown">
            <option value="S1">Semester 1</option>
            <option value="S2">Semester 2</option>
            <option value="S3">Semester 3</option>
            <option value="S4">Semester 4</option>
          </select>
        </div>
        <div class="four wide field">
          <label>Session</label>
          <select v-model="query.session"   @change="getModulesByFiliere"  name="session"  class="ui fluid dropdown">
            <option>normal</option>
            <option>rattrapage</option>
          </select>
        </div>
        <div class="four wide field">
          <label>Module</label>
          <select v-model="module"  @change="getNotesByAdmin"  name="module"  class="ui fluid dropdown">
            <option v-for="module in modules" :value="module._id.module">{{ module._id.module}}</option>
          </select>
        </div>
      </div>
    </form>
  </div>

    <table class="ui center aligned selectable celled table">
      <thead>
      <tr>
        <th>cin</th>
        <th>nom</th>
        <th>module</th>
        <th>semester</th>
        <th>annee scolaire</th>
        <th>NOTE</th>
        <th>REMARQUE</th>
        <th>--------</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="row in notes" @click="toggleNote(row)">
        <td>{{row.etudiant.cin}}</td>
        <td>{{row.etudiant.nom }} {{row.etudiant.prenom}}</td>
        <td>{{row.module}}</td>
        <td>{{row.semester}}</td>
        <td>{{row.anneeScolaire}}</td>
        <td class="positive" v-if="row.note > 10">{{row.note}} / 20</td>
        <td class="error" v-if="row.note < 10">{{row.note}} / 20</td>
        <td>{{row.observation}}</td>
        <td class="positive" v-if="row.note > 10"><b>VALIDER</b></td>
        <td class="error" v-if="row.note < 10"><b>NON VALIDER</b></td>
      </tr>
      </tbody>
      <tfoot class="full-width">
      <tr>
        <th colspan="99">
          <div class="ui right floated small positive button" @click="save">
            <i class="ui send icon"></i>Envoyer
          </div>
        </th>
      </tr>
      </tfoot>
      <div class="ui dimmer">
        <div class="content">
          <h3 class="ui inverted icon header">
            données introuvable
          </h3>
        </div>
      </div>
    </table>
    <div id="ADM_MODAL" class="ui large modal">
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
  name: "controler",
  data(){
    return{
      filieres:[],
      modules:[],
      notes:[],
      grades:[],
      query:{},
      filiere:{},
      current_post:{
        _id:'',
        grades:[]
      },
      session:'',
      module:'',
      semester:''
    }
  },
  methods:{
    toggleNote(post){
      this.grades = []
      $('#ADM_MODAL').modal('show')
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
            session : this.query.session,
          } ).then( data => {
        this.grades = data
      }, () => {})
    },
    save() {
      let notes = this.notes.map(e => {
        return {
          _id : e.etudiant.id,
          module : e.module,
          semester : e.semester,
          session : e.session,
          anneeScolaire : e.anneeScolaire,
          note : e.note,
          observation : e.observation
        }

      })
      this.$SHARED.dispatchAsyncPost(this.$SHARED.services.NOTES_ETD_CTRL,
          {
           notes : notes
          }).then(data => {
        this.message = data.message;
        this.$notify({
          group: 'crud',
          title: 'Success message',
          type: 'success',
          text: 'Les notes ont été distribuées avec succès !'
        });
      }),() => {
      }
    },
    getNotesByAdmin(){
      this.query.module = this.module
      this.$SHARED.dispatchAsyncPost(this.$SHARED.services.LISTER_NOTES_ADMIN,{
        query:this.query
      }).then(data => {
        this.notes = data;
      }, () => {

      })
    },
    getFilieres() {
      this.$SHARED.dispatchAsyncGet(this.$SHARED.services.LISTER_FILIERES).then(data => {
        this.filieres = data;
      }, () => {
      })
    },
    getModulesByFiliere(){
      delete this.query.module
      this.$SHARED.dispatchAsyncPost(this.$SHARED.services.LISTER_MODULES_BY_NOTES,{
        query:this.query
      }).then(data => {
        this.modules = data;
        if(this.modules.length === 0){
          $('.table')
              .dimmer('show')
              .dimmer({
                closable: false
              });
        }else{
          this.getNotesByAdmin()
          $('.table').dimmer('hide')
        }
      }, () => {
      })
    },
  },
  computed:{

  },
  mounted() {
    this.getFilieres()
  }
}
</script>

<style scoped>

</style>