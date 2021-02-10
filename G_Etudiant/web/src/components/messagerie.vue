<template>
  <div class="ui container">
    <div class="ui vertical fluid accordion menu">
      <div class="item">
        <a class="active title">
          <h3><i class="dropdown icon"></i>Boite messages</h3>
        </a>
        <div class="active content" style="margin: 20px">
          <table class="ui very basic fixed single line selectable table" v-if="messages.length !== 0">
            <tbody>
            <tr v-for="message in messages" @click="show(message)" >
              <td><i class="mail icon"></i> <b>From:</b> {{message.expediteur}}</td>
              <td><b>Subject:</b> {{message.objet}}</td>
              <td><b>{{ message.message }}</b> </td>
              <td class="right aligned"><b>{{ message.date }}</b></td>
            </tr>
            </tbody>
          </table>
          <div class="ui placeholder segment" v-else>
            <div class="ui icon header">
              <i class="pdf file outline icon"></i>
              Vous n'avez pas des messages.
            </div>
          </div>
        </div>
      </div>
    </div>
    <div id="messageViewer" class="ui modal">
      <div class="content">
        <h4 class="ui horizontal header divider">
          <a href="#">Reçu le {{ post.date }}</a>
        </h4>
        <div class="ui vertical stripe segment">
          <div class="ui text container">
            <img class="ui avatar image" src="../assets/images/avatar.jpg">
            <span> A partir de : <b>{{ post.expediteur }}</b></span>
            <h3 class="ui header">{{ post.objet }}</h3>
            <p>{{post.message}}</p>
          </div>
        </div>
      </div>
      <div class="actions">
        <reclamation
            v-bind:Destinataire="post.expediteur"
            v-bind:Expediteur="this.$store.state.user.nom +' '+ this.$store.state.user.prenom"
        ></reclamation>
      </div>
    </div>

  </div>
</template>

<script>
const reclamation = () => import("@/components/reclamation.vue")

export default {
  name: "messagerie",
  components: {
    reclamation
  },
  data(){
    return{
      user: this.$store.state.user,
      name: this.$store.state.user.nom+' '+this.$store.state.user.prenom,
      messages :[],
      post:{}
    }
  },
  methods:{
    show(message){
      this.post = JSON.parse(JSON.stringify(message))
      $('#messageViewer')
          .modal('show')
    },
    ListerMessages() {
      this.messages = []
      this.$SHARED.dispatchAsyncPost(this.$SHARED.services.RECEIVE_MESSAGE,{
        user:this.name
      }).then(data => {
        this.messages = data
      })
    },
  },
  mounted() {
    this.user=this.$store.state.user
    this.ListerMessages()
    $('.ui.accordion').accordion()
  }
}
</script>

<style scoped>

</style>