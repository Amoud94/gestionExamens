<template>
  <div class="ui container">
    <div class="ui pointing secondary menu">
      <a class="active item" data-tab="departements">Departements</a>
      <a class="item" data-tab="filieres">Filieres</a>
    </div>

    <div class="ui active tab segment" data-tab="departements">
      <div class="ui category search">
        <div class="ui icon input">
          <i class="search icon"></i>
          <input type="text" v-model="searchQuery" placeholder="Rechercher par nom :" />
        </div>
      </div>
      <table class="ui fluid selectable table">
            <thead>
            <tr>
              <th>Nom du departement</th>
              <th>Responsable du departement</th>
              <th>Email du departement</th>
              <th>Actions</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="row in resultQueryDepartement">
              <td>{{ row.nom }}</td>
              <td>{{ row.responsable }}</td>
              <td>{{ row.emailInstitutionnel }}</td>
              <td>
                <button class="compact circular ui medium icon positive  button" @click="toggleUpdateDepartement(row)"><i class="edit icon"></i></button>
                <button class="compact circular ui medium icon negative  button" @click="supprimerDepartement(row)"><i class="trash icon"></i></button>
              </td>
            </tr>
            </tbody>
            <tfoot class="full-width">
            <tr>
              <th colspan="10">
                <div class="ui  primary labeled icon button" @click="toggleInsertDepartement">
                  <i class="plus icon"></i> Ajouter un nouveau departement
                </div>
              </th>
            </tr>
            </tfoot>
          </table>
      <div id="Departement-Modal" class="ui large modal">
          <div class="content" style="padding: 40px">
            <h4 class="ui dividing header" v-if="current_post._id">Modifier les informations du departement : </h4>
            <h4 class="ui dividing header" v-else>Ajouter un nouveau departement : </h4>
              <form class="ui form">
                <div class="fields">
                  <div class="sixteen wide field">
                    <label>Nom du departement : </label>
                    <div class="field">
                      <input type="text"  v-model="current_post.nom"   placeholder="Nom du departement :">
                    </div>
                  </div>
                </div>
                <div class="fields">
                  <div class="sixteen wide field">
                    <label>Responsable du departement : </label>
                    <select v-model="current_post.responsable" class="ui fluid dropdown">
                      <option v-model="el.nom"  v-for="el in chefDepartements" :value="el.nom" >{{el.nom }}</option>
                    </select>
                  </div>
                </div>
                <div class="fields">
                  <div class="sixteen wide  field">
                    <label>Email institutionnel : </label>
                    <div class="field">
                      <input type="text"  v-model="current_post.emailInstitutionnel"   placeholder="Email institutionnel du departement">
                    </div>
                  </div>
                </div>
              </form>
          </div>
          <div class="actions">
            <div class="ui positive button" v-if="current_post._id" @click="modifierDepartement(current_post)">Enregistrer</div>
            <div class="ui primary button" v-else @click="ajouterDepartement(current_post)"><i class="save icon"></i>Enregistrer</div>

          </div>
        </div>
    </div>

    <div class="ui tab segment" data-tab="filieres">
      <div class="ui category search">
        <div class="ui icon input">
          <i class="search icon"></i>
          <input type="text" v-model="searchQueryFiliere" placeholder="Rechercher par nom :" />
        </div>
      </div>
      <table class="ui fluid selectable table">
        <thead>
        <tr>
          <th>Nom du filiere</th>
          <th>Responsable du filiere</th>
          <th>Email du filiere</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="row in resultQueryFiliere">
          <td>{{ row.nom }}</td>
          <td>{{ row.responsable }}</td>
          <td>{{ row.emailInstitutionnel }}</td>
          <td>
            <button class="compact circular ui medium icon positive button" @click="toggleUpdateFiliere(row)"><i class="edit icon"></i></button>
            <button class="compact circular ui medium icon negative  button" @click="supprimerFiliere(row)"><i class="trash icon"></i></button>
          </td>
        </tr>
        </tbody>
        <tfoot class="full-width">
        <tr>
          <th colspan="10">
            <div class="ui  primary labeled icon button" @click="toggleInsertFiliere">
              <i class="plus icon"></i> Ajouter une nouvelle filiere
            </div>
          </th>
        </tr>
        </tfoot>
      </table>
      <div id="Filiere-Modal" class="ui large modal">
        <div class="content" style="padding: 40px">
          <h4 class="ui dividing header" v-if="current_filiere._id">Modifier les informations du filiere : </h4>
          <h4 class="ui dividing header" v-else>Ajouter une nouvelle filiere : </h4>
            <form class="ui form">
              <div class="fields">
                <div class="sixteen wide field">
                  <label>Nom du filiere : </label>
                  <div class="field">
                    <input type="text" placeholder="nom du filiere" v-model="current_filiere.nom" >
                  </div>
                </div>
              </div>
              <div class="fields">
                <div class="sixteen wide field">
                  <label>Chef du filiere : </label>
                  <select v-model="current_filiere.responsable" class="ui fluid dropdown">
                    <option :value="el.nom" v-for="el in chefFilieres" >{{el.nom }}</option>
                  </select>
                </div>
              </div>
              <div class="fields">
                <div class="sixteen wide  field">
                  <label>Email institutionnel : </label>
                  <div class="field">
                    <input type="text"  v-model="current_filiere.emailInstitutionnel"   placeholder="Email institutionnel du filiere">
                  </div>
                </div>
              </div>
            </form>
        </div>
        <div class="actions">
          <div class="ui positive button" v-if="current_filiere._id" @click="modifierFiliere(current_filiere)">Enregistrer</div>
          <div class="ui primary button" v-else @click="ajouterFiliere(current_filiere)"><i class="save icon"></i>Enregistrer</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "departements",

  data() {
    return {
      current_post: {},
      current_filiere: {},
      searchQuery:'',
      searchQueryFiliere:'',

      departements:[],
      filieres:[],
      chefDepartements:[],
      chefFilieres:[],

      displayData:'',
      message: '',
    }
  },
  computed: {
    resultQueryDepartement() {
      if (this.searchQuery) {
        return this.departements.filter((item) => {
          return this.searchQuery.toLowerCase().split(' ').every(v => item.nom.toLowerCase().includes(v))
        })
      } else {
        return this.departements;
      }
    },
    resultQueryFiliere() {
      if (this.searchQueryFiliere) {
        return this.filieres.filter((item) => {
          return this.searchQueryFiliere.toLowerCase().split(' ').every(v => item.nom.toLowerCase().includes(v))
        })
      } else {
        return this.filieres;
      }
    }
  },
  methods: {
    toggleUpdateDepartement(post) {
      this.current_post = JSON.parse(JSON.stringify(post));
      $('#Departement-Modal').modal('show')
    },
    toggleInsertDepartement() {
      this.current_post={}
      $('#Departement-Modal').modal('show')
    },

    toggleUpdateFiliere(post) {
      this.current_filiere = JSON.parse(JSON.stringify(post));
      $('#Filiere-Modal').modal('show')
    },
    toggleInsertFiliere() {
      this.current_filiere={}
      $('#Filiere-Modal').modal('show')
    },

    ajouterFiliere(current_filiere) {
      this.$SHARED.dispatchAsyncPost(this.$SHARED.services.AJOUTER_FILIERE,
          {
            ...current_filiere,
          }).then( data => {
        this.message = data.message;
        this.listerFilieres()
        this.$notify({
          group: 'crud',
          title: 'Success message',
          type: 'success',
          text: 'Ajouté avec succès !'
        });
        $('#Filiere-Modal').modal('hide')
      }, () => {})
    },
    supprimerFiliere(post) {
      this.$SHARED.dispatchAsyncDelete(this.$SHARED.services.SUPPRIMER_FILIERE,
          {_id : post._id}).then(data => {
        this.message = data.message
        this.listerFilieres()
        this.$notify({
          group: 'crud',
          title: 'Warning message',
          type: 'warn',
          text: 'Supprimé avec succès !'
        });
      }, () => {})
    },
    modifierFiliere(current_filiere){
      this.$SHARED.dispatchAsyncPut(this.$SHARED.services.MODIFIER_FILIERE,
          {
            ...current_filiere,
          }).then( data => {
        this.message = data.message;
        this.listerFilieres()
        $('#Filiere-Modal').modal('hide')
        this.$notify({
          group: 'crud',
          title: 'Success message',
          type: 'success',
          text: 'Modifié avec succès !'
        });

      }, () => {})
    },
    listerFilieres(){
      this.$SHARED.dispatchAsyncGet(this.$SHARED.services.LISTER_FILIERES ).then( data => {
        this.filieres = data;
      }, () => {})
    },

    ajouterDepartement(current_post) {
      this.$SHARED.dispatchAsyncPost(this.$SHARED.services.AJOUTER_DEPARTEMENT,
          {
            ...current_post
          }).then( data => {
        this.message = data.message
        this.ListerDepartements()
        $('#Departement-Modal').modal('hide')
        this.$notify({
          group: 'crud',
          title: 'Success message',
          type: 'success',
          text: 'Ajouté avec succès !'
        });
      }, () => {})
    },
    supprimerDepartement(post) {
      this.$SHARED.dispatchAsyncDelete(this.$SHARED.services.SUPPRIMER_DEPARTEMENT,
          {_id : post._id}).then(data => {
        this.ListerDepartements()
        this.message = data.message
        this.$notify({
          group: 'crud',
          title: 'Warning message',
          type: 'warn',
          text: 'Supprimé avec succès !'
        });
      }, () => {})
    },
    modifierDepartement(current_post){
      this.$SHARED.dispatchAsyncPut(this.$SHARED.services.MODIFIER_DEPARTEMENT,
          {
            ...current_post
          }).then( data => {
        this.message = data.message;
        this.ListerDepartements()
        $('#Departement-Modal').modal('hide')
        this.$notify({
          group: 'crud',
          title: 'Success message',
          type: 'success',
          text: 'Modifié avec succès !'
        });
      }, () => {})
    },
    ListerDepartements(){
      this.$SHARED.dispatchAsyncGet(this.$SHARED.services.LISTER_DEPARTEMENTS ).then( data => {
        this.departements = data;
      }, () => {})
    },

    ListerChefFiliere(){
      this.$SHARED.dispatchAsyncGet(this.$SHARED.services.LISTER_CHEFS_FILIERE ).then( data => {
        this.chefFilieres = data;
      }, () => {})
    },
    ListerChefDepartement(){
      this.$SHARED.dispatchAsyncGet(this.$SHARED.services.LISTER_CHEFS_DEPARTEMENT ).then( data => {
        this.chefDepartements = data;
      }, () => {})
    }
  },
  mounted() {
    this.listerFilieres();
    this.ListerDepartements();
    this.ListerChefDepartement();
    this.ListerChefFiliere();

    $('.container .menu .item')
        .tab()
    ;
  }
}
</script>

<style scoped>

</style>