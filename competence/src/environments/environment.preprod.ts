export const environment = {
  production: false,
  authConfig: {
    instance: 'https://login.microsoftonline.com/',
    tenant: '92e84ceb-fbfd-47ab-be52-080c6b87953f',
    tenentId: '92e84ceb-fbfd-47ab-be52-080c6b87953f',
    clientId: '4e6160fe-d3c2-4856-bfe3-33448903a78c',
    redirectUri: 'https://isfpreprod.internal.ericsson.com/Competence_PreProd',
    navigateToLoginRequestUrl: false,
    cacheLocation: 'localStorage'
  },
  GLOBAL_API_URL : "https://isfuatservices.internal.ericsson.com:8443/isf-rest-server-java_PP/",
 rootDir:"/Competence_PreProd/",
 logoutRedirect:"https://isfpreprod.internal.ericsson.com/",
 LMTestingEmail:""
};
