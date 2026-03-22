import axios from 'axios';

// Service base URLs - direct to backend services for development
const USER_SERVICE = 'http://localhost:8082';
const TRANSLATION_SERVICE = 'http://localhost:8081';

// Auth API client
const authApi = axios.create({
  baseURL: USER_SERVICE,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Translation API client
const translationApi = axios.create({
  baseURL: TRANSLATION_SERVICE,
  headers: {
    'Content-Type': 'application/json',
  },
});

// Add auth token interceptor for both clients
const addAuthInterceptor = (apiClient) => {
  apiClient.interceptors.request.use(
    (config) => {
      const token = localStorage.getItem('token');
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
      return config;
    },
    (error) => Promise.reject(error)
  );

  apiClient.interceptors.response.use(
    (response) => response,
    (error) => {
      if (error.response?.status === 401) {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        window.location.href = '/login';
      }
      return Promise.reject(error);
    }
  );
};

addAuthInterceptor(authApi);
addAuthInterceptor(translationApi);

// Auth APIs
export const login = (data) => authApi.post('/api/auth/login', data);
export const register = (data) => authApi.post('/api/auth/register', data);
export const getProfile = () => authApi.get('/api/auth/profile');

// Translation APIs
export const translate = (data) => translationApi.post('/api/translate', data);
export const getLanguages = () => translationApi.get('/api/languages');
export const getTranslationHistory = (page = 0, size = 20) =>
  translationApi.get(`/api/translation/history?page=${page}&size=${size}`);
export const getTranslationCount = () => translationApi.get('/api/translation/count');
