<template>
  <div>
    <div class="ui tiny menu" style="margin-bottom: 50px">
      <router-link to="/Etudiant/notes" class="header item"><i class="ui icon list"></i>Verifier les notes</router-link>
      <router-link to="/Etudiant/reclamation" class="header item"><i class="ui icon write"></i>Poser une reclamation</router-link>
      <router-link to="/Etudiant/messagerie" class="header item"><i class="ui icon send"></i>Messagerie</router-link>
      <div class="right menu">
        <router-link to="" class="header right aligned item">
          <div @click="show" class="ui primary button">
            <i class="ui user icon"></i> Profile
          </div>
        </router-link>
      </div>
    </div>
    <div class="ui right wide inverted vertical menu sidebar" style="text-align: center ; padding: 10px">
      <a class="item" style=" padding-bottom: 40px">
        <div class="field">
          <div class="ui medium circular image">
            <img src="../assets/images/avatar.jpg" style="margin: auto" class="visible content">
          </div>
        </div>
      </a>
      <div class="item">
        <h5>CIN : {{this.user.cin}}</h5>
      </div>
      <div class="item">
        <h5>Etudiant(e) : {{this.name}}</h5>
      </div>
      <div class="item">
        <h5>Filiere : {{this.user.filiere}}</h5>
      </div>
      <div class="item">
        <br><br><br>
      </div>
      <div class="item" @click="done">
        <router-link to="/" class="header right aligned item" >logout</router-link>
      </div>
    </div>
    <div class="pusher">
      <!-- Site content !-->
      <router-view/>
    </div>

  </div>

</template>

<script>
export default {
  name: "Etudiant",
  components: {},

  data() {
    return {
      user:{},
      name:''
    }
  },
  methods:{
    show(){
      $('.ui.sidebar')
          .sidebar('show')
      ;
    },
    done(){
      $('.ui.sidebar')
          .sidebar('hide')
      ;
    }
  },
  mounted() {
    this.user = this.$store.state.user
    this.name = this.user.nom +' '+ this.user.prenom
  }
}
</script>

<style scoped>

</style>
