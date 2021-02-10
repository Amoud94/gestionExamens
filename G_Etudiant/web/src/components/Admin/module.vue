<template>
	<div class="ui container">
    <div class="ui pointing secondary menu">
      <a class="active item" data-tab="Modules">Modules</a>
      <a class="item" data-tab="Elements">Elements du Module</a>
    </div>
    <div class="ui active tab segment" data-tab="Modules">
        <div id="modules-modale" class="ui large modal">
          <div class="content" style="padding: 40px">
            <h4 class="ui dividing header" v-if="current_post._id">Modifier module : </h4>
            <h4 class="ui dividing header" v-else>Ajouter un nouveau module : </h4>
              <form class="ui form">
                <div class="fields">
                    <div class="six wide field">
                      <label>Module name : </label>
                      <input type="text" v-model="current_post.nom" placeholder="Module Name">
                    </div>
                    <div class="ten wide field">
                      <label>Description :</label>
                      <textarea rows="3"  v-model="current_post.description"  placeholder="Description"></textarea>
                    </div>
                </div>
                <div class="two fields">
                    <div class="field">
                      <label>Departement :</label>
                      <select v-model="current_post.departement" class="ui fluid dropdown">
                        <option selected disabled value="">SVP selectionnez un departement</option>
                        <option v-for="d in departements" >{{ d.nom}}</option>
                      </select>
                    </div>
                  <div class="field">
                    <label>Filiere</label>
                    <select v-model="current_post.filiere" class="ui fluid dropdown">
                      <option selected disabled >SVP selectionnez une filiere</option>
                      <option v-for="d in filieres" >{{ d.nom}}</option>
                    </select>
                  </div>
                </div>
                <div class="two fields">

                  <div class="field">
                    <label>Année Scolaire</label>
                    <select id="anneeScolaire" name="anneeScolaire" v-model="current_post.anneeScolaire"  class="ui fluid dropdown"></select>
                  </div>
                  <div class="field">
                    <label>Semester</label>
                    <select v-model="current_post.semester" class="ui fluid dropdown">
                      <option value="S1">Semester 1</option>
                      <option value="S2">Semester 2</option>
                      <option value="S3">Semester 3</option>
                      <option value="S4">Semester 4</option>
                    </select>
                  </div>
                </div>

              </form>

            </div>
          <div class="actions">
            <div class="ui positive button" v-if="current_post._id" @click="ModifierModule(current_post)">Enregistrer !!</div>
            <div class="ui medium primary icon button" v-else @click="AjouterModule(current_post)"><i class="save icon"></i>Enregistrer</div>
          </div>
        </div>
        <div class="ui category search">
          <form class="ui form">
            <div class="fields">
              <div class="four wide field">
                <label>Filiere</label>
                <select v-model="filiere"  @change="getModulesByFiliere" name="filiere"  class="ui fluid dropdown">
                  <option selected disabled >SVP selectionnez une filiere</option>
                  <option v-for="d in filieres" >{{ d.nom}}</option>
                </select>
              </div>
              <div class="four wide field">
                <label>Search</label>
                <div class="ui icon input">
                  <i class="search icon"></i>
                  <input type="text" v-model="searchQuery" placeholder="Rechercher par nom " />
                </div>
              </div>
            </div>
          </form>
        </div>
        <table class="ui selectable celled table">
          <thead>
          <tr>
            <th> Module </th>
            <th> Departement </th>
            <th> Filiere </th>
            <th> Semester </th>
            <th> Description</th>
            <th> </th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="row in resultQuery" >
            <td>{{ row.nom }}</td>
            <td>{{ row.departement }}</td>
            <td>{{ row.filiere }}</td>
            <td>{{ row.semester }}</td>
            <td>{{ row.description }}</td>

            <td>
              <button class="compact circular ui medium icon  positive button" @click="toggleUpdate(row)"><i class="edit icon"></i></button>
              <button class="compact circular ui medium icon  negative button" @click="SupprimerModule(row)"><i class="trash icon"></i></button>
            </td>
          </tr>
          </tbody>
          <tfoot class="full-width">
          <tr>
            <th colspan="10">
              <div class="ui  primary labeled icon button" @click="toggleInsert">
                <i class="plus icon"></i> Ajouter un nouveau module
              </div>
            </th>
          </tr>
          <tr>
            <th colspan="99">
              <div>
                <pagination :limit="limit" :current_page="page" :count="count" @setPage="setPage" slot="pagination"/>
              </div>
            </th>
          </tr>
          </tfoot>
        </table>
    </div>
    <div class="ui tab segment" data-tab="Elements">
      <div id="elements-modale" class="ui large modal">
        <div class="content" style="padding: 40px">
          <h4 class="ui dividing header" v-if="current_post._id">Modifier element : </h4>
          <h4 class="ui dividing header" v-else>Ajouter un nouveau element : </h4>
          <form class="ui form">
            <div class="fields">
              <div class="six wide field">
                <label>Nom :</label>
                <input type="text"   v-model="current_post.nom" maxlength="16" placeholder="Nom d'element">
              </div>
              <div class="twelve wide field">
                <label>Description :</label>
                <textarea rows="3"  v-model="current_post.description"  placeholder="Description"></textarea>
              </div>
            </div>
            <div class="fluid wide field">
              <label>Module d'elemet :</label>
              <select v-model="current_post.module" class="ui fluid dropdown">
                <option selected disabled value="">SVP selectionnez module d'element</option>
                <option v-for="item in data" :value="item">{{ item.nom}}</option>
              </select>
            </div>
            <div class="fluid wide fields">
              <div class="eight wide field">
                <label>Coefficient d'elemet :</label>
                <input type="text"   v-model="current_post.coefficient" maxlength="3" placeholder="Coefficient">
              </div>
              <div class="eight wide field">
                <label>Nombre d'heure :</label>
                <input type="text"   v-model="current_post.nbrHeure" maxlength="3" placeholder="Nombre d'heure">
              </div>

            </div>

          </form>

        </div>
        <div class="actions">
          <div class="ui positive button" v-if="current_post._id" @click="ModifierElement(current_post)">Enregistrer !!</div>
          <div class="ui medium primary icon button" v-else @click="AjouterElement(current_post)">Enregistrer<i class="save icon"></i></div>
        </div>
      </div>
      <div class="ui category search">
      <form class="ui form">
        <div class="fields">
          <div class="four wide field">
            <label>Module</label>
            <select v-model="module"  @change="getElementsByModule" name="module"  class="ui fluid dropdown">
              <option v-for="module in data" :value="module.nom" >{{ module.nom}}</option>
            </select>
          </div>
          <div class="four wide field">
            <label>Search</label>
            <div class="ui icon input">
              <i class="search icon"></i>
              <input type="text" v-model="searchElement" placeholder="Rechercher par nom " />
            </div>
          </div>
        </div>
      </form>
      </div>
      <table class="ui fluid selectable celled table">
        <thead>
        <tr>
          <th> Element </th>
          <th> Module </th>
          <th> Filiere </th>
          <th> Semester </th>
          <th> Annee universitaire </th>
          <th> Coefficient </th>
          <th> Nombre d'heure  </th>
          <th>   </th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="row in filtreElements" >
          <td>{{ row.nom }}</td>
          <td>{{ row.module.nom }}</td>
          <td>{{ row.module.filiere }}</td>
          <td>{{ row.module.semester }}</td>
          <td>{{ row.module.anneeScolaire }}</td>
          <td>{{ row.coefficient }}</td>
          <td>{{ row.nbrHeure }}</td>

          <td>
            <button class="compact circular ui medium icon  positive button" @click="toggleUpdateElement(row)"><i class="edit icon"></i></button>
            <button class="compact circular ui medium icon  negative button" @click="SupprimerElement(row)"><i class="trash icon"></i></button>
          </td>
        </tr>
        </tbody>
        <tfoot class="full-width">
        <tr>
          <th colspan="10">
            <div class="ui  primary labeled icon button" @click="toggleInsertElement">
              <i class="plus icon"></i> Ajouter un nouveau element
            </div>
          </th>
        </tr>
        </tfoot>
      </table>
    </div>
  </div>
</template>

<script>
const pagination = () => import("@/components/pagination.vue")
	export default {
		name: "module",
		components: {
		  pagination
    },
		data() {
			return {
        page: 1,
        limit: 10,
        count: 0,

        message: '',
        filiere: '',
        module: '',
        searchQuery: '',
        searchElement: '',
        current_post:{},
        post_view:{},
        departements:[],
        filieres:[],
        modules:[],
        data:[],
        elements:[]
      }
		},
    methods: {

      setPage(page){
        this.page = page;
        this.getModulesByFiliere();
      },

      toggleInsert(){
        this.current_post = {};
        $('#modules-modale').modal('show')
      },
      toggleUpdate(post) {
        this.current_post = JSON.parse(JSON.stringify(post));
        $('#modules-modale').modal('show')
      },

      toggleInsertElement(){
        this.current_post = {};
        $('#elements-modale').modal('show')
      },
      toggleUpdateElement(post) {
        this.current_post = JSON.parse(JSON.stringify(post));
        $('#elements-modale').modal('show')
      },

      getDerpartement(){
        this.$SHARED.dispatchAsyncGet(this.$SHARED.services.LISTER_DEPARTEMENTS ).then( data => {
          this.departements = data;
        }, () => {})
      },
      getFilieres(){
        this.$SHARED.dispatchAsyncGet(this.$SHARED.services.LISTER_FILIERES ).then( data => {
          this.filieres = data;
        }, () => {})
      },

      getModules(){
        this.$SHARED.dispatchAsyncGet(this.$SHARED.services.LISTER_MODULES).then( data => {
          this.modules = data;
          this.data = data;
        }, () => {})
      },

      getModulesByFiliere(){
        this.$SHARED.dispatchAsyncPost(this.$SHARED.services.LISTER_MODUlES_BY_FILIERE,{
          filiere: this.filiere,
          page: this.page,
          limit: this.limit
        }  ).then( data => {
          this.modules = data.docs;
          this.count = data.count;
        }, () => {})
      },

      getElements(){
        this.$SHARED.dispatchAsyncGet(this.$SHARED.services.LISTER_ELEMENTS).then( data => {
          this.elements = data;
        }, () => {})
      },
      getElementsByModule(){
        this.$SHARED.dispatchAsyncPost(this.$SHARED.services.LISTER_ELEMENTS_BY_MODULE,{
          module : this.module
        }).then( data => {
          this.elements = data;
        }, () => {})
      },

      AjouterElement(current_post) {
        current_post.coefficient = parseInt(current_post.coefficient)
        this.$SHARED.dispatchAsyncPost(this.$SHARED.services.AJOUTER_ELEMENT,
            {
              ...current_post,
            }).then( data => {
          this.message = data.message;
          this.getElements()
          this.current_post={}
          $('#elements-modale').modal('hide')
          this.$notify({
            group: 'crud',
            title: 'Success message',
            type: 'success',
            text: 'Ajouté avec succès !'
          });
        }, () => {})
      },
      ModifierElement(current_post){
        current_post.coefficient = parseInt(current_post.coefficient)
        this.$SHARED.dispatchAsyncPut(this.$SHARED.services.MODIFIER_ELEMENT,
            {
              ...current_post,
            }).then( data => {
          this.message = data.message;
          this.getElements()
          this.current_post={}
          $('#elements-modale').modal('hide')
          this.$notify({
            group: 'crud',
            title: 'Success message',
            type: 'success',
            text: 'Modifié avec succès !'
          });
        }, () => {})
      },
      SupprimerElement(current_post){
        this.$SHARED.dispatchAsyncDelete(this.$SHARED.services.SUPPRIMER_ELEMENT,
            {_id : current_post._id}).then(data => {
          this.message = data.message
          this.getElements()
          this.$notify({
            group: 'crud',
            title: 'Warning message',
            type: 'warn',
            text: 'Supprimé avec succès !'
          });
        }, () => {})
      },

      AjouterModule(current_post) {
        this.$SHARED.dispatchAsyncPost(this.$SHARED.services.AJOUTER_MODULE,
            {
              ...current_post,
            }).then( data => {
          this.message = data.message;
          this.current_post={}
          this.getModules()
          $('#modules-modale').modal('hide')
          this.$notify({
            group: 'crud',
            title: 'Success message',
            type: 'success',
            text: 'Ajouté avec succès !'
          });
        }, () => {})

      },
      ModifierModule(current_post){
        this.$SHARED.dispatchAsyncPut(this.$SHARED.services.MODIFIER_MODULE,
            {
              ...current_post,
            }).then( data => {
          this.message = data.message;
          this.getModules()
          this.current_post={}
          $('#modules-modale').modal('hide')
          this.$notify({
            group: 'crud',
            title: 'Success message',
            type: 'success',
            text: 'Modifié avec succès !'
          });
        }, () => {})
      },
      SupprimerModule(current_post){
        this.$SHARED.dispatchAsyncDelete(this.$SHARED.services.SUPPRIMER_MODULE,
            {_id : current_post._id}).then(data => {
          this.message = data.message
          this.getModules()
          this.$notify({
            group: 'crud',
            title: 'Warning message',
            type: 'warn',
            text: 'Supprimé avec succès !'
          });
        }, () => {})
      },
      getYear(){
        var min = new Date().getFullYear(),
            max = min + 5,
            select = document.getElementById('anneeScolaire')
        for (var i = min; i<=max; i++){
          var opt = document.createElement('option');
          let j = i+1
          opt.innerHTML = i +" / "+ j;
          select.appendChild(opt);
        }
      }
    },
    computed: {
      resultQuery()
      {
        if (this.searchQuery) {
          return this.modules.filter((item) => {
            return this.searchQuery.toLowerCase().split(' ').every(v => item.nom.toLowerCase().includes(v))
          })
        } else {
          return this.modules;
        }
      },

      filtreElements()
      {
        if (this.searchElement) {
          return this.elements.filter((item) => {
            return this.searchElement.toLowerCase().split(' ').every(v => item.nom.toLowerCase().includes(v))
          })
        } else {
          return this.elements;
        }
      }
    },
    mounted() {
      this.getElements();
      this.getModules();
      this.getDerpartement();
      this.getFilieres();
      this.getYear()
      $('.container .menu .item')
          .tab()
      ;
    }
	}
</script>

<style scoped>
</style>
