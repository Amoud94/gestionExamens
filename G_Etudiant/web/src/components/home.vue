<template>

  <div id="login" class="ui middle aligned center aligned grid login-bg">
    <div class="column">
      <h2 class="ui teal image header">
        <div class="content">
          Connectez-vous à votre compte
        </div>
      </h2>
      <form class="ui large form">
        <div class="ui stacked segment">
          <div class="field">
            <div class="ui left icon input">
              <i class="user icon"></i>
              <input v-model="email" type="text" name="email" placeholder="E-mail address">
            </div>
          </div>
          <div class="field">
            <div class="ui left icon input">
              <i class="lock icon"></i>
              <input v-model="cin" type="password" name="password" placeholder="CIN">
            </div>
          </div>
          <div class="ui fluid large teal submit button" @click.prevent="onLoginClick">Login</div>
        </div>
      </form>
    </div>
  </div>

</template>

<script>
export default {
  name: "home",
  data() {
    return {
      email: "",
      cin: ""
    };
  },
  methods: {
    onLoginClick() {
      log("`onLoginClick`");
      this.$SHARED.dispatchAsyncPost(this.$SHARED.services.LOGIN, {
        username:  this.email,
        login:  this.cin
      }).then(data => {
          this.$store.dispatch("setToken",data.TOKEN)
          this.$store.dispatch("setUser",data.user)
          this.$notify({
            group: "auth",
            title: "AUTHENTIFIE AVEC SUCCES",
            type: "success", // warn , error, success, info
            text: "Bienvenu monsieur  " + data.user.nom + " !"
          });
          if(this.$store.state.user.position === "Administrateur"){
            console.log(0)
            this.$router.push("/AdminDashboard/modules");
          }else{
            console.log(1)
            this.$router.push("/EnseignantDashBoard/consulterEtudiants")
          }

      }, () => {
        this.$notify({
          group: 'auth',
          title: 'Authentification message',
          type: 'error',
          text: 'échec de l\'authentification, veuillez vérifier votre login / mot de passe !'
        });
      });
    }
  }
};
</script>

<style scoped>

#login {
  margin-top: 110px;
}
.login-bg {
  background-image: url("./../assets/images/home-images/keyboard.jpg");
  background-size: cover;
  padding-bottom: 50px;
  padding-top: 50px;
}

.column {
  max-width: 450px;
}
</style>
