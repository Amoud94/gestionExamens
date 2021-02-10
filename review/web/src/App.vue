<template>
	<div id="app">
    <router-view/>
    <notifications group="crud" />
    <notifications group="auth" position="top center"/>
	</div>
</template>

<script>
	import "../libs/semantic-ui/semantic.min.js";
	import "../libs/semantic-ui/semantic.min.css";

	export default {
		name: "App"
	};
</script>

<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>

<style>
	*:not(i) {
		font-family: 'Roboto-Condensed',
		sans-serif !important;
	}
</style>
