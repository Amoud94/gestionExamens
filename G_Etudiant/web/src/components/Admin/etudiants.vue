<template>
	<div class="ui container">

    <div id="etudiants-modal" class="ui large modal">
      <div class="content" style="padding: 40px">
        <h4 class="ui dividing header" v-if="current_post._id">Modifier les informations personnel d'etudiant : </h4>
        <h4 class="ui dividing header" v-else>Ajouter un nouveau etudiant : </h4>
          <form class="ui form">
            <div class="fields">
              <div class="eight wide field">
                <label>Nom : </label>
                  <input type="text"  v-model="current_post.nom"    placeholder="Nom d'etudiant :">
              </div>
              <div class="eight wide field">
                <label>Prenom : </label>
                  <input type="text"  v-model="current_post.prenom"   placeholder="Prenom d'etudiant :">
              </div>
            </div>
            <div class="fields">
              <div class="seven wide  field">
                <label>Lieu naissance : </label>
                <select  v-model="current_post.lieuNaissance" name="lieuNaissance" class="ui fluid dropdown">
                  <option value="Casablanca">Casablanca</option>
                  <option value="Rabat">Rabat</option>
                  <option value="Tanger">Tanger</option>
                  <option value="Beni Mellal">Beni Mellal</option>
                </select>
              </div>
              <div class="seven wide field">
                <label>Date naissance : </label>
                <datepicker :format="format" v-model="current_post.dateNaissance" name="dateNaissance" placeholder="Select Date"></datepicker>
              </div>
              <div class="four wide  field">
                <label>Sexe : </label>
                <select  v-model="current_post.sexe" name="sexe" class="ui fluid dropdown">
                  <option value="male">M</option>
                  <option value="female">F</option>
                </select>
              </div>
              <div class="field">
                <label>Filiere</label>
                <select v-model="current_post.filiere" name="filiere"  class="ui fluid dropdown">
                  <option selected disabled >SVP selectionnez une filiere</option>
                  <option v-for="d in filieres" >{{ d.nom}}</option>
                </select>
              </div>
            </div>
            <div class="fields">
              <div class="twelve wide field">
                <label>Numero du telephone : </label>
                <input type="text"  v-model="current_post.telephone"  name="telephone"  placeholder="Numero du telephone personnel :">
              </div>
              <div class="four wide field">
                <label>CIN : </label>
                <input type="text"  v-model="current_post.cin"  name="CIN"  placeholder="Numero de la carte nationnal :">
              </div>
            </div>
            <div class="sixteen wide field">
              <label>Email personnel: </label>
              <input type="text"  v-model="current_post.emailPersonnel" name="emailPersonnel"   placeholder="Email personnel du enseignant">
            </div>
            <div class="sixteen wide field">
              <label>Email institutionnel: </label>
              <input type="text"  v-model="current_post.emailInstitutionnel"  name="emailInstitutionnel" placeholder="Email institutionnel du enseignant">
            </div>
          </form>
      </div>
      <div class="actions">
        <div class="ui positive submit button" v-if="current_post._id" @click="Modifier(current_post)">Enregistrer</div>
        <div class="ui medium primary icon button" v-else @click="Ajouter(current_post)"><i class="save icon"></i>Enregistrer</div>
      </div>
    </div>

    <div id="viewModal" class="ui fluid modal">
      <div class="content">
        <div class="ui grid">
          <div class="five wide column">
          <div class="ui card" >
            <a class="image" href="#">
              <img src="../../assets/images/avatar.jpg">
            </a>
          </div>
            <reclamation
                v-bind:Destinataire="post_view.nom +' '+ post_view.prenom"
                v-bind:Expediteur="this.$store.state.user.nom +' '+ this.$store.state.user.prenom"
            ></reclamation>
          </div>
          <div class="eleven wide column">
            <div class="row">

              <div class="fields">
                <div class="field">
                  <div class="ui fluid large label">
                    <label type="text">Nom et prenom d'etudiant : {{post_view.nom +' '+post_view.prenom }}</label>
                  </div>
                </div>
                <br>
                <div class="field">
                  <div class="ui fluid large label">
                    <label type="text">CIN : {{post_view.cin}}</label>
                  </div>
                </div>
                <br>
                <div class="field">
                  <div class="ui fluid large  label">
                    <label type="text">Filiere : {{post_view.filiere}}</label>
                  </div>
                </div>
                <br>
                <div class="field">
                  <div class="ui fluid large label">
                    <label type="text">Sexe : {{post_view.sexe}}</label>
                  </div>
                </div>

                <br>
                <div class="field">
                  <div class="ui fluid large label">
                    <label type="text">Telephone : {{post_view.telephone}}</label>                  </div>
                </div>
                <br>
                <div class="field">
                  <div class="ui fluid large  label">
                    <label type="text">Lieu de naissance : {{post_view.lieuNaissance}}</label>
                  </div>
                </div>
                <br>
                <div class="field">
                <div class="ui fluid large label">
                    <label type="text">Date de naissance : {{post_view.dateNaissance}}</label>
                  </div>
                </div>
                <br>
                <div class="field">
                  <div class="ui fluid large label">
                    <label type="text">Email personnel d'etudiant : {{post_view.emailPersonnel}}</label>
                  </div>
                </div>
                <br>
                <div class="field">
                  <div class="ui fluid large label">
                    <label type="text">Email institutionnel d'etudiant  : {{post_view.emailInstitutionnel}}</label>
                  </div>
                </div>
                <br>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="ui category search">
      <form class="ui form">
        <div class="fields">
        <div class="four wide field">
          <label>Filiere</label>
          <select v-model="filiere"  @change="studentByFiliere" name="filiere"  class="ui fluid dropdown">
            <option selected disabled >SVP selectionnez une filiere</option>
            <option v-for="d in filieres" >{{ d.nom}}</option>
          </select>
        </div>
          <div class="six wide field">
            <label>Search</label>
            <div class="ui icon input">
              <i class="search icon"></i>
              <input type="text" v-model="searchQuery" placeholder="Rechercher par nom " />
            </div>
          </div>
        </div>
      </form>
    </div>

    <table class="ui fluid selectable celled table">
      <thead>
      <tr>
        <th> Nom</th>
        <th>Prenom</th>
        <th>Telephone</th>
        <th>Filiere</th>
        <th>Email personnel</th>
        <th>Email Institutionnel</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="row in resultQuery" >
        <td @click="toggleView(row)">{{ row.nom }}</td>
        <td>{{ row.prenom }}</td>
        <td>{{ row.telephone }}</td>
        <td>{{ row.filiere }}</td>
        <td>{{ row.emailPersonnel }}</td>
        <td>{{ row.emailInstitutionnel }}</td>
        <td>
          <button class="compact circular ui medium icon  positive button" @click="toggleUpdate(row)"><i class="edit icon"></i></button>
          <button class="compact circular ui medium icon  negative button" @click="supprimer(row)"><i class="trash icon"></i></button>
        </td>
      </tr>
      </tbody>
      <tfoot class="full-width">
      <tr>
        <th colspan="10">
          <div class="ui  primary labeled icon button" @click="toggleInsert">
            <i class="plus icon"></i> Ajouter un nouveau etudiant
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
	const reclamation = () => import("@/components/reclamation.vue")
	import Datepicker from "vuejs-datepicker/dist/vuejs-datepicker.esm.js";
	export default {
    name: "etudiants",
    components: {
      Datepicker,
	    pagination,
      reclamation
    },
    data() {
      return {
        query:{},
        format: 'yyyy-MM-dd',
        current_post: {},
        post_view: {},
        message: '',
        filiere: '',
        searchQuery: '',
        searchOption: '',
        etudiants: [],
        filieres: [],
		page: 1,
		limit: 10,
		count: 0,
      }
    },
    methods: {
	    setPage(page){
		    this.page = page;
		    this.studentByFiliere();
	    },

      toggleView(post) {
        this.post_view = JSON.parse(JSON.stringify(post));
        $('#viewModal').modal('show')
      },
      toggleUpdate(post) {
        this.current_post = JSON.parse(JSON.stringify(post));
        $('#etudiants-modal').modal('show')
      },
      toggleInsert() {
        this.current_post = {};
        $('#etudiants-modal').modal('show')
      },

      supprimer(post) {
        this.$SHARED.dispatchAsyncDelete(this.$SHARED.services.SUPPRIMER_ETUDIANT,
            {_id: post._id}).then(data => {
          this.message = data.message;
          this.getALL()
          this.$notify({
            group: 'crud',
            title: 'Warning message',
            type: 'warn',
            text: 'Supprimé avec succès !'
          });
        }, () => {
        })
      },
      Ajouter(current_post) {
	      current_post.notes =[]
        this.$SHARED.dispatchAsyncPost(this.$SHARED.services.AJOUTER_ETUDIANT, {
          ...current_post,
        }).then(data => {
          this.message = data.message;
          $('#etudiants-modal').modal('hide')
          this.current_post={}
          this.getALL()
          this.$notify({
            group: 'crud',
            title: 'Success message',
            type: 'success',
            text: 'Ajouté avec succès !'
          });
        }, () => {
        })
      },
      Modifier(current_post) {
        this.$SHARED.dispatchAsyncPut(this.$SHARED.services.MODIFIER_ETUDIANT,
            {
              ...current_post,
            }).then(data => {
          this.message = data.message;
          $('#etudiants-modal').modal('hide')
          this.getALL()
          this.current_post={}
          this.$notify({
            group: 'crud',
            title: 'Success message',
            type: 'success',
            text: 'Modifié avec succès !'
          });
        }, () => {
        })
      },

      getALL() {
        this.$SHARED.dispatchAsyncGet(this.$SHARED.services.LISTER_ETUDIANTS).then(data => {
          this.etudiants = data;
        }, () => {
        })
      },

      studentByFiliere() {
        this.$SHARED.dispatchAsyncPost(this.$SHARED.services.LISTER_STUDENTS_BY_FILIERE,
            {
              filiere : this.filiere,
              page: this.page,
              limit: this.limit
            } ).then( data => {
          this.etudiants = data.docs;
          this.count = data.count;
        }, () => {})
      },

      getFilieres() {
        this.$SHARED.dispatchAsyncGet(this.$SHARED.services.LISTER_FILIERES).then(data => {
          this.filieres = data;
        }, () => {
        })
      },

    },
    computed: {
      resultQuery() {
        if (this.searchQuery) {
          return this.etudiants.filter((item) => {
            return this.searchQuery.toLowerCase().split(' ').every(v => item.nom.toLowerCase().includes(v))
          })
        } else {
          return this.etudiants;
        }
      }
    },
    mounted() {
      this.getFilieres()
      this.getALL()
    }
  }
</script>

<style scoped>
</style>
