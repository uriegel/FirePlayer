<template>
    <div class="viewer">
        <video ref="video" controls autoplay :src=url @loadedmetadata="onLoadedMeta"></video>
    </div>
</template>  

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator'

@Component({
  	components: {}
})export default class VideoViewer extends Vue {
    url = ""
    mounted() {
        this.url = decodeURI(window.location.search.substring(7))
        ;(this.$refs.video as HTMLElement).focus()
    }
    onLoadedMeta(evt: Event) {
        console.log("Geladen", event)
        event?.target
        console.log("Weite", (event?.target as any).videoWidth, "Höhe", (event?.target as any).videoHeight)
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