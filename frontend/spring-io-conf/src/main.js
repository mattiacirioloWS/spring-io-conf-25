import {mount} from 'svelte'
import './app.css'
import App from './app.html'

const app = mount(App, {
    target: document.getElementById('app'),
})

export default app
