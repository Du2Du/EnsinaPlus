import PrimeUI from 'tailwindcss-primeui';
import type { Config } from 'tailwindcss';

const config = {
    content: ['./src/**/*.{html,ts,scss,css}', './index.html', 'node_modules/primeng/**/*.{html,ts}'],
    theme: {
        extend: {},
    },
    plugins: [
        PrimeUI,
    ],
    important: true
};

export default config as Config;
