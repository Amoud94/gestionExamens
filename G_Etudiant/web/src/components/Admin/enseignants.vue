<template>

  <div class="ui container" id="enseignants">

    <div id="enseignant-modal" class="ui large modal">
      <div class="content" style="padding: 40px">
          <form class="ui form">
            <h4 class="ui dividing header" v-if="current_post._id">Modifier les informations personnel du enseignant : </h4>
            <h4 class="ui dividing header" v-else>Ajouter un nouveau enseignant : </h4>
            <br>
            <div class="fields">
              <div class="eight wide field">
                <label>Nom : </label>
                <input type="text"  v-model="current_post.nom"   placeholder="Nom du enseignant :">
              </div>
              <div class="six wide field">
                <label>Prenom : </label>
                <input type="text"  v-model="current_post.prenom"   placeholder="Prenom du enseignant :">
              </div>
              <div class="six wide field">
                <label>Position occupée : </label>
                <select  v-model="current_post.permissions" @change="onChange(current_post.permissions)" class="ui fluid dropdown">
                  <option selected disabled value="">Position :</option>
                  <option  v-for="item in permissions"  :value="item.value" >{{item.label}}</option>
                </select>
              </div>
            </div>
            <div class="field" v-if="isRM">
              <label class="typo__label">Modules : </label>
              <multiselect v-model="current_post.modules" label="nom" track-by="_id" placeholder="Type to search" open-direction="bottom" :options="modules" :multiple="true" :searchable="true" :internal-search="false" :clear-on-select="false" :close-on-select="false" :options-limit="300" :limit="4" :max-height="600" :show-no-results="false" :hide-selected="true">
                <template slot="ui tag" slot-scope="{ option, remove }">
                  <span class="custom__tag">{{ option.nom }}<span class="custom__remove" @click="remove(option)"><i class="delete icon"></i></span></span>
                </template>
                <span slot="noResult">Oops! Pas d'element trouver.</span>
              </multiselect>
            </div>
            <div class="fields">
              <div class="ten wide field">
                <label>Numero du telephone : </label>
                <input type="text"  v-model="current_post.telephone"  placeholder="Numero du telephone d'enseignant :">
              </div>
              <div class="six wide field">
                <label>CIN : </label>
                <input type="text"  v-model="current_post.cin"   placeholder="CIN :">
              </div>
              <div class="six wide field" v-if="!isCD">
                <label>Departement : </label>
                <select  v-model="current_post.departement" class="ui fluid dropdown">
                  <option  v-for="item in departements"  :value="item.nom" >{{item.nom}}</option>
                </select>
              </div>
            </div>
            <div class="fields">
              <div class="eight wide field">
                <label>Email personnel: </label>
                <input type="text"  v-model="current_post.emailPersonnel"   placeholder="Email personnel du enseignant">
              </div>
              <div class="eight wide field">
                <label>Email institutionnel: </label>
                <input type="text"  v-model="current_post.emailInstitutionnel"   placeholder="Email institutionnel du enseignant">
              </div>
            </div>
            <div class="field" v-if="current_post._id">
              <label class="typo__label">Elements : </label>
              <multiselect v-model="current_post.elements" label="nom" track-by="_id" placeholder="Type to search" open-direction="bottom" :options="elements" :multiple="true" :searchable="true" :internal-search="false" :clear-on-select="false" :close-on-select="false" :options-limit="300" :limit="4" :max-height="600" :show-no-results="false" :hide-selected="true">
                <template slot="ui tag" slot-scope="{ option, remove }">
                  <span class="custom__tag">{{ option.nom }}<span class="custom__remove" @click="remove(option)"><i class="delete icon"></i></span></span>
                </template>
                <span slot="noResult">Oops! Pas d'element trouver.</span>
              </multiselect>
            </div>
            <div class="field" v-else>
                <div class="ui toggle checkbox">
                  <input v-model="IF_EE" type="checkbox" name="public">
                  <label>Va-t-il être responsable de l'enseignement ?</label>
                </div>
            </div>
            <div class="field" v-if="IF_EE">
              <label class="typo__label">Elements : </label>
              <multiselect v-model="current_post.elements" label="nom" track-by="_id" placeholder="Type to search" open-direction="bottom" :options="elements" :multiple="true" :searchable="true" :internal-search="false" :clear-on-select="false" :close-on-select="false" :options-limit="300" :limit="4" :max-height="600" :show-no-results="false" :hide-selected="true">
                <template slot="ui tag" slot-scope="{ option, remove }">
                  <span class="custom__tag">{{ option.nom }}<span class="custom__remove" @click="remove(option)"><i class="delete icon"></i></span></span>
                </template>
                <span slot="noResult">Oops! Pas d'element trouver.</span>
              </multiselect>
            </div>
          </form>
      </div>
      <div class="actions">
        <div class="ui positive button" v-if="current_post._id" @click="Modifier(current_post)">Enregistrer</div>
        <div class="ui medium primary icon button" v-else @click="Ajouter(current_post)"><i class="save icon"></i>Enregistrer</div>
      </div>
    </div>
    <div class="ui category search">
      <form class="ui form">
        <div class="fields">
          <div class="four wide field">
            <label>Departement</label>
            <select v-model="query.departement"  @change="enseignantByDepartement"  class="ui fluid dropdown">
              <option  v-for="item in departements" >{{item.nom}}</option>
            </select>
          </div>
          <div class="four wide field">
            <label>Position</label>
            <select v-model="query.position"  @change="enseignantByDepartement"  class="ui fluid dropdown">
              <option  v-for="item in permissions" >{{item.label}}</option>
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
          <th>CIN</th>
          <th>Nom</th>
          <th>Prenom</th>
          <th>Telephone</th>
          <th>Email personnel</th>
          <th>Email institutionnel</th>
          <th>Position occupée</th>
          <th>Departement</th>
          <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="row in resultQuery">
          <td>{{ row.cin }}</td>
          <td>{{ row.nom }}</td>
          <td>{{ row.prenom }}</td>
          <td>{{ row.telephone }}</td>
          <td>{{ row.emailPersonnel }}</td>
          <td>{{ row.emailInstitutionnel }}</td>
          <td>{{ row.position }}</td>
          <td>{{ row.departement }}</td>
          <td>
            <button class="compact circular ui medium icon positive button" @click="toggleUpdate(row)"><i class="edit icon"></i></button>
            <button class="compact circular ui medium icon negative  button" @click="Supprimer(row)"><i class="trash icon"></i></button>
          </td>
        </tr>
        </tbody>
        <tfoot class="full-width">
        <tr>
          <th colspan="99">
            <div class="ui primary labeled icon button" @click="toggleInsert">
              <i class="plus icon"></i> Ajouter un nouveau enseignant
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

</template>

<script>
const pagination = () => import("@/components/pagination.vue")
import Multiselect from 'vue-multiselect'

	export default {
    name: "enseignants",
    components: {
      Multiselect,
      pagination
    },
    data() {
      return {
        page: 1,
        limit: 10,
        count: 0,

        permissions: [
          {
            label: "Chef de departement",
            value: ["PRMSS_EE","PRMSS_CD"]
          },
          {
            label: "Chef de filiere",
            value: ["PRMSS_EE","PRMSS_CF"]
          },
          {
            label: "Responsable de module",
            value: ["PRMSS_EE","PRMSS_RM"]
          },
          {
            label: "Enseignant d'un element",
            value: ["PRMSS_EE"]
          }
        ],
        current_post: {},
        searchQuery: '',
        IF_EE: '',
        position: '',
        message: '',
        query: {},
        enseignants: [],
        modules: [],
        departements: [],
        filieres: [],
        elements: []
      }
    },
    computed: {

      isRM(){
        if(this.current_post && this.current_post.permissions ){
          return this.current_post.permissions.indexOf('PRMSS_RM') !== -1
        }else{
          return false
        }
      },
      isCF(){
        if(this.current_post && this.current_post.permissions ){
          return this.current_post.permissions.indexOf('PRMSS_CF') !== -1
        }else{
          return false
        }
      },
      isCD(){
        if(this.current_post && this.current_post.permissions ){
          return this.current_post.permissions.indexOf('PRMSS_CD') !== -1
        }else{
          return false
        }
      },
      resultQuery() {
        if (this.searchQuery) {
          return this.enseignants.filter((item) => {
            return this.searchQuery.toLowerCase().split(' ').every(v => item.nom.toLowerCase().includes(v))
          })
        } else {
          return this.enseignants;
        }
      }
    },
    methods: {
      setPage(page){
        this.page = page;
        this.Lister();
      },

      Ajouter(current_post) {
        this.$SHARED.dispatchAsyncPost(this.$SHARED.services.AJOUTER_ENSEIGNANT,
            {
              ...current_post,
              position: this.position
            })
            .then(data => {
              this.message = data.message;
              this.Lister();
              this.current_post={}
              $('#enseignant-modal').modal('hide')
              this.$notify({
                group: 'crud',
                title: 'Success message',
                type: 'success',
                text: 'Ajouté avec succès !'
              });
            }, () => {})
      },
      Modifier(current_post) {
        let value;
        if(this.position){
          value = this.position
        }else{
          value = current_post.position
        }
        this.$SHARED.dispatchAsyncPut(this.$SHARED.services.MODIFIER_ENSEIGNANT,
            {
              ...current_post,
              position: value
            }).then(data => {
          this.message = data.message;
          this.Lister();
          this.current_post = {};
          $('#enseignant-modal').modal('hide')
          this.$notify({
            group: 'crud',
            title: 'Success message',
            type: 'success',
            text: 'Modifié avec succès !'
          });
        }, () => {})
      },
      Supprimer(current_post) {
        this.$SHARED.dispatchAsyncDelete(this.$SHARED.services.SUPPRIMER_ENSEIGNANT,
            {_id: current_post._id}).then(data => {
          this.message = data.message;
          this.Lister();
          this.$notify({
            group: 'crud',
            title: 'Warning message',
            type: 'warn',
            text: 'Supprimé avec succès !'
          });
        }, () => {})
      },

      Lister() {
        this.$SHARED.dispatchAsyncGet(this.$SHARED.services.LISTER_ENSEIGNANTS+'/'+this.page+'/'+this.limit).then(data => {
          this.enseignants = data.docs;
          this.count = data.count;
        }, () => {})
      },

      enseignantByDepartement() {
        this.$SHARED.dispatchAsyncPost(this.$SHARED.services.LISTER_ENSEIGNANT_BY_DEPARTEMENT,
            {
              query : this.query,
              page: this.page,
              limit: this.limit
            } ).then( data => {
          this.enseignants = data.docs;
          this.count = data.count;
        }, () => {})
      },

      getElements(){
        this.$SHARED.dispatchAsyncGet(this.$SHARED.services.LISTER_ELEMENTS).then(data => {
          this.elements = data;
        }, () => {
        })
      },
      getModules(){
        this.$SHARED.dispatchAsyncGet(this.$SHARED.services.LISTER_MODULES).then(data => {
          this.modules = data;
        }, () => {
        })
      },
      getDepartement(){
        this.$SHARED.dispatchAsyncGet(this.$SHARED.services.LISTER_DEPARTEMENTS).then(data => {
          this.departements = data;
        }, () => {
        })
      },
      getFilieres(){
        this.$SHARED.dispatchAsyncGet(this.$SHARED.services.LISTER_FILIERES).then(data => {
          this.filieres = data;
        }, () => {
        })
      },

      onChange(value){
        if(value != null){
            if(value.indexOf('PRMSS_CD') !== -1){
              this.position = "Chef de departement"
            }else if(value.indexOf('PRMSS_CF') !== -1){
              this.position = "Chef de filiere"
            }else if(value.indexOf('PRMSS_RM') !== -1){
              this.position = "Responsable de module"
            }else if(value.indexOf('PRMSS_EE') !== -1){
              this.position = "Enseignant d'un element"
            }
        }
      },
      toggleUpdate(post) {
        this.current_post = JSON.parse(JSON.stringify(post));
        if(this.current_post){
          $('#enseignant-modal').modal('show')
        }
      },
      toggleInsert() {
        this.current_post = {};
        $('#enseignant-modal').modal('show')
      },

    },

    mounted() {


      this.Lister();
      this.getElements();
      this.getModules();
      this.getDepartement();
    }

  }
</script>

<style scoped>

</style>
