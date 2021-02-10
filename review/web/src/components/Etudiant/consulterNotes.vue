<template>
  <div class="ui container">
    <div class="ui vertical fluid accordion menu">
      <div class="item">
        <a class="active title">
          <h3><i class="dropdown icon"></i>Session normal</h3>
        </a>
        <div id="SN"  class="active content">
          <div class="ui cards">
            <div class="ui card" v-for="note in notes_SN">

              <div class="content">

                <div class="description" style="margin-top: 30px">

                  <div class="column">
                    <p class="ui dividing tiny header">module : {{ note.module }}</p>
                    <p>Session : {{ note.session }}</p>
                    <p>Semester : {{ note.semester }}</p>
                    <p>Annee scolaire : {{ note.anneeScolaire }}</p>
                  </div>

                </div>
              </div>
              <div class="ui teal top right attached label" v-if="note.note > 10">Note : {{ note.note }} / 20</div>
              <div class="ui red top right attached label" v-if="note.note < 10">Note : {{ note.note }} / 20</div>

              <div class="ui bottom attached button" @click="toggleNote(note)">
                <i class="add icon"></i>
                Verifier
              </div>
            </div>
          </div>
          <div class="ui fluid placeholder segment" v-if="notes_SN.length === 0">
            <div class="ui icon header">
              <i class="pdf file outline icon"></i>
              Vous n'avez pas des notes , session rattrapage.
            </div>
          </div>
        </div>
      </div>
      <div class="item">
        <a class="title">
          <h3><i class="dropdown icon"></i>Session rattrapage</h3>
        </a>
        <div id="SR" class="content">
          <div class="ui cards">
            <div class="ui card" v-for="note in notes_SR">

              <div class="content">

                <div class="header">{{ note.module  }}</div>
                <div class="description" style="padding: 10px">

                  <div class="column">
                    <p class="ui dividing tiny header">module : {{ note.module }}</p>
                    <p>Session : {{ note.session }}</p>
                    <p>Semester : {{ note.semester }}</p>
                    <p>Annee scolaire : {{ note.anneeScolaire }}</p>
                  </div>

                </div>
              </div>
              <div class="ui teal top right attached label" v-if="note.note > 10">Note : {{ note.note }} / 20</div>
              <div class="ui red top right attached label" v-if="note.note < 10">Note : {{ note.note }} / 20</div>

              <div class="ui bottom attached button" @click="toggleNote(note)">
                <i class="add icon"></i>
                Verifier
              </div>
            </div>
          </div>
          <div class="ui fluid placeholder segment" v-if="notes_SR.length === 0">
            <div class="ui icon header">
              <i class="pdf file outline icon"></i>
              Vous n'avez pas des notes , session rattrapage.
            </div>
          </div>
        </div>
      </div>
    </div>
    <div id="SUP_MODAL" class="ui large modal">
      <div class="content">
        <div style="margin: 20px">
          <table class="ui center aligned selectable celled table">
            <thead>
            <tr>
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
  name: "consulterNotes",
  data() {
    return{
      current_post: {},
      user:{},
      notes:[],
      grades:[],
      notes_SN:[],
      notes_SR:[]
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
            nom : this.user.nom,
            prenom : this.user.prenom,
            semester : post.semester,
            anneeScolaire : post.anneeScolaire,
            session : post.session,
          } ).then( data => {
        this.grades = data
      }, () => {})
    },
  },
  mounted() {
    this.user=this.$store.state.user
    this.notes=this.$store.state.user.notes

    $('.ui.accordion').accordion()

    let data = this.notes
    if(data){
      data.forEach(e =>{
        if(e.session === 'normal'){
          this.notes_SN.push(e)
        }else{
          this.notes_SR.push(e)
        }
      })
    }
  }
}
</script>

<style scoped>
    .title{
      margin-bottom: 20px;
    }
    .ui .card{
      margin: 10px 5px 30px 30px;
    }
</style>