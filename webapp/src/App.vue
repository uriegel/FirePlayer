<template>
   	<video-viewer />
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'
import VideoViewer from './components/VideoViewer.vue'

@Component({
  	components: {
    	VideoViewer
  	},
})
export default class App extends Vue {
}
</script>
<style>
body {
    overflow:hidden;
    height: 100vh;
    padding: 0px;
    margin: 0px;    
}
</style>
