import {defineConfig} from 'vite'
import {sveltekit} from "@sveltejs/kit/vite";

export default defineConfig({
    plugins: [sveltekit()],
    server: {
        proxy: {
            '/api': 'http://localhost:8080'
        }
    }
})
