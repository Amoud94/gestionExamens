<template>
  <div>
    <div class="ui tiny menu" style=" margin-bottom: 50px">
      <router-link to="/AdminDashboard/modules" class="header item"><i class="ui icon book"></i>Modules</router-link>
      <router-link to="/AdminDashboard/departements" class="header item"><i class="ui icon clipboard"></i>Departements</router-link>
      <router-link to="/AdminDashboard/enseignants" class="header item"><i class="ui icon address book"></i>Teachers</router-link>
      <router-link to="/AdminDashboard/etudiants" class="header item"><i class="ui icon address book"></i>Students</router-link>
      <router-link to="/AdminDashboard/controler" class="header item"><i class="ui icon list"></i>Controler</router-link>
      <router-link to="/AdminDashboard/messagerie" class="header item"><i class="ui icon send"></i>Messagerie</router-link>
      <router-link to="/" class="header right aligned item"><i class="ui icon logout"></i>logout</router-link>
    </div>
    <router-view/>
  </div>
</template>

<script>

export default {
  name: "DashboardAdmin"
};
</script>

<style>

</style>