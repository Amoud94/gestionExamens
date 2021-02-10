<template>
  <div class="ui container">

    <form class="ui form">
      <div class="fields">
        <div class="three wide field">
          <label>Element : </label>
          <select v-model="element" @change="getNotes" class="ui dropdown">
            <option v-for="element in elements"  :value="element">{{ element.nom }} </option>
          </select>
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
            <input   type="text" :value="element.module.semester">
          </div>
        </div>
        <div class="three wide field">
          <label>Session : </label>
          <select v-model="session"  @change="getNotes" class="ui dropdown">
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
        <th>cin</th>
        <th>nom</th>
        <th>prenom</th>
        <th>element</th>
        <th>module</th>
        <th>semester</th>
        <th>session</th>
        <th>année scolaire</th>
        <th>NOTE</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="row in resultQuery"  @click="toggleNote(row)">
        <td>{{row.etudiant.cin }}</td>
        <td>{{row.etudiant.nom }}</td>
        <td>{{row.etudiant.prenom}}</td>
        <td>{{row.element.nom}}</td>
        <td>{{row.element.module.nom }}</td>
        <td>{{row.element.module.semester }}</td>
        <td>{{row.session}}</td>
        <td>{{row.element.module.anneeScolaire }}</td>
        <td class="positive" v-if="row.note > 10">{{row.note}} / 20</td>
        <td class="error" v-if="row.note < 10">{{row.note}} / 20</td>
      </tr>
      </tbody>
    </table>
    <div id="note-modal" class="ui large modal">
      <div class="header">
        <div class="ui slider checkbox">
          <input type="checkbox" v-model="update" name="update">
          <label v-if="update">Modification activer</label>
          <label v-else>Activer Modification</label>
        </div>
      </div>
      <div class="content">
        <div style="margin: 20px">
          <form class="ui form" v-if="current_post">
            <div class="fields">
              <div class="four wide field">
                <label>CIN:</label>
                <input type="text" v-if="current_post.etudiant" disabled v-model="current_post.etudiant.cin">
              </div>
              <div class="seven wide field" v-if="current_post">
                <label>Nom d'etudiant(e):</label>
                <input type="text" v-if="current_post.etudiant" disabled v-model="current_post.etudiant.nom">
              </div>
              <div class="seven wide field">
                <label>Prenom d'etudiant(e):</label>
                <input type="text" v-if="current_post.etudiant"  disabled v-model="current_post.etudiant.prenom">
              </div>
            </div>
            <div class="fields" v-if="update">
              <div class="six wide field">
                <label>Elements du module: </label>
                <select v-model="current_post.element" class="ui dropdown">
                  <option v-for="element in elements"  :value="element">{{ element.nom}} </option>
                </select>
              </div>

              <div class="six wide field" >
                <label>Module: </label>
                <div class="ui disabled input">
                  <input  type="text" v-if="current_post.element" :value="current_post.element.module.nom">
                </div>
              </div>

              <div class="four wide field" >
                <label>Coefficient: </label>
                <div class="ui disabled input">
                  <input  type="text" v-if="current_post.element" :value="current_post.element.coefficient">
                </div>
              </div>
            </div>
            <div class="fields" v-else>
              <div class="six wide field">
                <label>Element:</label>
                <input type="text" v-if="current_post.element" disabled v-model="current_post.element.nom">
              </div>
              <div class="six wide field">
                <label>Module:</label>
                <input type="text" v-if="current_post.element" disabled v-model="current_post.element.module.nom">
              </div>
              <div class="four wide field">
                <label>Coefficient:</label>
                <input type="text" v-if="current_post.element" disabled v-model="current_post.element.coefficient">
              </div>
            </div>
            <div class="fields">
              <div class="ten wide field">
                <label>Description:</label>
                <textarea rows="3" v-if="current_post.element" disabled v-model="current_post.element.description"></textarea>
              </div>
              <div class="six wide field" v-if="current_post.enseignant">
                <label>Enseignant:</label>
                <input type="text"  disabled :value="current_post.enseignant.nom +' '+ current_post.enseignant.prenom">
              </div>
            </div>
            <div class="fields" v-if="update">
              <div class="six wide field">
                <label>Session:</label>
                <select v-model="current_post.session" class="ui dropdown">
                  <option > normal </option>
                  <option > rattrapage </option>
                </select>
              </div>
              <div class="six wide field">
                <label>Note:</label>
                <input type="text"  v-model="current_post.note">
              </div>
              <div class="six wide field">
                <label>Observation:</label>
                <input type="text"  v-model="current_post.observation">
              </div>
            </div>
            <div class="fields" v-else>
              <div class="six wide field">
                <label>Session:</label>
                <input type="text" disabled v-model="current_post.session">
              </div>
              <div class="six wide field">
                <label>Note:</label>
                <input type="text" disabled v-model="current_post.note">
              </div>
              <div class="six wide field">
                <label>Observation:</label>
                <input type="text" disabled v-model="current_post.observation">
              </div>
            </div>
          </form>
        </div>
      </div>
      <div class="actions">
        <div class="ui positive button" @click="ModifierNote(current_post)" v-if="update"><i class="save icon"></i>Enregistrer</div>
        <div class="ui negative button" @click="SupprimerNote(current_post)" v-else > <i class="trash icon"></i>Supprimer</div>
      </div>
    </div>
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
export default {
  name: "consulterNotes",
  data() {
    return {
      elements:[],
      notes:[],
      element: {},
      session:'',
      current_post:{},
      update:'',
      searchQuery:'',
    }
  },
  methods:{
    toggleNote(post){
      this.current_post=JSON.parse(JSON.stringify(post))
      $('#note-modal').modal('show')
    },
    getNotes(){
      if(this.element){
        console.log(1)
        this.$SHARED.dispatchAsyncPost(this.$SHARED.services.LISTER_NOTES,
            {
              enseignant : this.$store.state.user._id,
              filiere : this.element.module.filiere,
              session : this.session,
              semester : this.element.module.semester,
              element : this.element.nom
            } ).then( data => {
          this.notes = data
        }, () => {})
      }
    },
    ModifierNote(current_post){
      this.$SHARED.dispatchAsyncPut(this.$SHARED.services.MODIFIER_NOTE,
          {
            ...current_post,
          }).then( data => {
        this.message = data.message;
        this.current_post={}
        $('#note-modal .slider.checkbox').checkbox('uncheck')
        this.update = false
        this.getNotes()
        $('#note-modal').modal('hidde')
      }, () => {})
    },
    SupprimerNote(current_post){
      this.$SHARED.dispatchAsyncDelete(this.$SHARED.services.SUPPRIMER_NOTE,
          {_id : current_post._id}).then(data => {
        this.message = data.message
        this.current_post={}
        this.getNotes()
        $('#note-modal').modal('hidde')
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
    this.elements = this.$store.state.user.elements
    if(this.elements){
      $('.container').dimmer('hide').dimmer({closable: false});
    }else{
      $('.container').dimmer('show').dimmer({closable: false});
    }
  }
}
</script>

<style scoped>

</style>