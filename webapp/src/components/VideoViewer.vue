<template>
    <div>
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
video {
    width: 100vw;
}

</style>