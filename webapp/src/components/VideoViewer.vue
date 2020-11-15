<template>
    <div class="viewer">
        <video controls autoplay :src=url></video>
    </div>
</template>  

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'

@Component({
  	components: {}
})export default class VideoViewer extends Vue {
    url = "https://uriegel.de/video/Get out.mp4"
    mounted() {
        this.url = decodeURI(window.location.search.substring(7))
    }
}
</script>

<style scoped>
.viewer {
    background-color: black;
    width: 100vw;
    height: 100vh;
    position: relative;
}
video {
    position: absolute;
    outline-width: 0px;
    background-color: black;
    width: 100%;
    height: 100%;
}
</style>