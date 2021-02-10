<template>
  <div>
    <div id="reclamation" class="ui basic modal">
      <div class="content">
        <div class="ui form">
          <div class="content" >
            <div class="two fields">
              <div class="field">
                <div class="ui disabled input" style="background-color: #912D2B">
                  <input  disabled type="text" :value="'À :  ' + Destinataire">
                </div>
              </div>
              <div class="field">
                <div class="ui disabled input" style="background-color: #912D2B">
                  <input  disabled type="text" :value="'À partir de :  '+Expediteur">
                </div>
              </div>
            </div>
            <div class="field">
              <input type="text" v-model="Objet" placeholder="Sujet...">
            </div>
            <div class="field">
              <textarea rows="6"  v-model="Message"  placeholder="Message"></textarea>
            </div>
          </div>
        </div>
      </div>
      <div class="actions">
        <div class="ui red basic cancel inverted button">
          <i class="remove icon"></i>
          Annuler
        </div>
        <div class="ui green ok inverted button" @click="save">
          <i class="checkmark icon"></i>
          Envoyer
        </div>
      </div>
    </div>
    <div class="ui green ok inverted button" @click="toggle">
      <i class="reply icon"></i>
      Repondre
    </div>
  </div>
</template>

<script>
export default {
  name: "reclamation",
  data(){
    return {
      Objet:'',
      Message:'',
    }
  },
  methods: {
    save(){
      let date = new Date()
      this.$SHARED.dispatchAsyncPost(this.$SHARED.services.SEND_MESSAGE,
          {
            destinataire: this.Destinataire,
            expediteur:this.Expediteur,
            objet:this.Objet,
            message:this.Message,
            date: date.toISOString().slice(0, 10)
          }).then(data => {
        this.Objet='';
        this.Message=''
        this.Destinataire=''
        this.Expediteur=''
        this.$notify({
          group: 'crud',
          title: 'Success message',
          type: 'warn',
          text: 'Message envoyé avec succès !'
        });
      }, () => {
      })
    },
    toggle(){
      $('#reclamation')
          .modal('show')
      ;
    }
  },
  props: ['Destinataire','Expediteur'],

}
</script>

<style scoped>

</style>