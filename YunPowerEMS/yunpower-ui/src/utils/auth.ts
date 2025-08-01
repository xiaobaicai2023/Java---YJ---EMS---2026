const TOKEN_KEY = 'token'
const TokenHandler = 'tokenHandler'

const isLogin = () => {
  return !!localStorage.getItem(TOKEN_KEY)
}

const getToken = () => {
  return localStorage.getItem(TOKEN_KEY)
}

const setToken = (token: string) => {
  localStorage.setItem(TOKEN_KEY, token)
}

const clearToken = () => {
  localStorage.removeItem(TOKEN_KEY)
}

const getTokenHandler = () => {
  if (
    localStorage.getItem(TokenHandler) === null ||
    localStorage.getItem(TokenHandler) === undefined ||
    localStorage.getItem(TokenHandler) === ''
  ) {
    return 'Authorization'
  }
  return localStorage.getItem(TokenHandler)
}

const setTokenHandler = (tokenHandler: string) => {
  localStorage.setItem(tokenHandler, tokenHandler)
}

const clearTokenHandler = () => {
  localStorage.removeItem(TokenHandler)
}

export {
  isLogin,
  getToken,
  setToken,
  clearToken,
  getTokenHandler,
  setTokenHandler,
  clearTokenHandler,
}
