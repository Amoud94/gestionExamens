<template>
  <div class="ui container">
    <div class="ui container" style="padding-bottom: 40px">
      <form class="ui form">
        <br>
        <div class="fields">
          <div class="eight wide field">
            <label>MODULE : </label>
            <select v-model="module" @change="getElementsByModule" class="ui dropdown">
              <option v-for="module in modules" >{{ module}} </option>
            </select>
          </div>
          <div class="eight wide field">
            <label>ELEMENT : </label>
            <select v-model="element" @change="getEnseignantByElement" class="ui dropdown">
              <option v-for="element in elements" :value="element.nom" >{{ element.nom}} </option>
            </select>
          </div>
          <div class="six wide field" v-if="enseignant.nom">
            <label>Enseignant d'element : </label>
            <div class="ui disabled input">
              <input type="text" :value="enseignant.nom +' '+enseignant.prenom ">
            </div>
          </div>
        </div>
        <div class="field">
            <input type="text" v-model="subject" placeholder="Sujet...">
        </div>
        <div class="field">
          <textarea rows="10"  v-model="motif"  placeholder="Motif de reclamation"></textarea>
        </div>
      </form>
    </div>
      <div class="actions">
        <div class="ui  primary labeled icon button" @click="send">
          <i class="send icon"></i> Envoyer reclamation
        </div>
      </div>
    </div>
</template>

<script>
export default {
  name: "poserReclamation",
  data(){
    return{
      user:{},
      notes:[],
      filiere:'GRH',
      modules : [],
      elements : [],
      module :'',
      subject :'',
      motif :'',
      element :'',
      enseignant : {}
    }
  },
  methods: {
    send(){
      this.$notify({
        group: 'crud',
        title: 'Success message',
        type: 'success',
        text: 'Reclamation envoyer avec succès !'
      });
      this.module=''
      this.subject=''
      this.motif=''
      this.element=''
      this.enseignant={}
    },
    getElementsByModule(){
      this.$SHARED.dispatchAsyncPost(this.$SHARED.services.LISTER_ELEMENTS_BY_MODULE,{
        module : this.module
      }).then( data => {
        this.elements = data;
      }, () => {})
    },

    getEnseignantByElement(){
      this.$SHARED.dispatchAsyncPost(this.$SHARED.services.GET_ENSEIGNANT_BY_ELEMENT,{
        element : this.element
      }).then( data => {
        this.enseignant = data;
      }, () => {})
    },
    getModules(){
      let elements = this.notes
      let uniqueArray = []
      if(elements){
        console.log(1)
        elements.forEach(element =>{
          console.log(element)
          uniqueArray.push( element.module)
        })
        this.modules = [...new Set(uniqueArray)]
      }
    }
  },
  mounted() {
    this.user = this.$store.state.user
    this.notes = this.$store.state.user.notes
    this.getModules()
  }
}
</script>

<style scoped>

</style>