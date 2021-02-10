<template>
  <div class="ui container">
    <div class="ui vertical fluid accordion menu">
      <div class="item">
        <a class="active title">
          <h3><i class="dropdown icon"></i>Boite messages</h3>
        </a>
        <div class="active content" style="margin: 20px">
          <table class="ui very basic fixed single line selectable table">
            <tbody>
            <tr v-for="message in messages" @click="show(message)" >
              <td><i class="mail icon"></i> <b>From:</b> {{message.expediteur}}</td>
              <td><b>Subject:</b> {{message.objet}}</td>
              <td><b>{{ message.message }}</b> </td>
              <td class="right aligned"><b>{{ message.date }}</b></td>
            </tr>
            </tbody>
          </table>
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
            <img class="ui avatar image" src="../../assets/images/avatar.jpg">
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
      name:this.$store.state.user.nom+' '+this.$store.state.user.prenom,
      user:this.$store.state.user,
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
      this.$SHARED.dispatchAsyncPost(this.$SHARED.services.LISTER_MESSAGES,{
        user:this.name
      }).then(data => {
        this.messages = data
      })
    },
  },
  mounted() {
    this.ListerMessages()
    this.user=this.$store.state.user
    $('.ui.accordion').accordion()
    let date = new Date()
    console.log(date.toISOString().slice(0, 10))
  }
}
</script>

<style scoped>

</style>