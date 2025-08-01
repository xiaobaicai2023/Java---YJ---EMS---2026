import { mergeConfig } from 'vite'
// import eslint from 'vite-plugin-eslint'
import baseConfig from './vite.config.base'
export default mergeConfig(
  {
    mode: 'development',
    server: {
      host: '0.0.0.0',
      port: '8888',
      open: true,
      fs: {
        strict: true,
      },
      proxy:{
        '/dev-api': {
          //target:"http://127.0.0.1:8080",
          target:"http://112.31.119.29:9284",
          changeOrigin: true,
          rewrite: (path) => path.replace(/^\/dev-api/, '')
        }
      }
    },
    plugins: [
      // eslint({
      //   cache: false,
      //   include: ['src/**/*.ts', 'src/**/*.tsx', 'src/**/*.vue'],
      //   exclude: ['node_modules'],
      // }),
    ]
  },
  baseConfig
)
