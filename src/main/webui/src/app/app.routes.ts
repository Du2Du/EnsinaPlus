import { Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { HomeComponent } from './pages/home/home.component';
import { authorizationGuard } from './guards/authorization.guard';
import { redirectAuthenticationGuard } from './guards/redirect-authentication.guard';

export const routes: Routes = [
    {
        path: '',
        title: 'Dashboard',
        component: DashboardComponent,
        canActivate: [redirectAuthenticationGuard]
    },
    {
        path: 'home',
        title: 'Home',
        component: HomeComponent,
        canActivate: [authorizationGuard]
    },
    {
        path: 'login',
        title: 'Login',
        component: LoginComponent,
        canActivate: [redirectAuthenticationGuard]
    },
    {
        path: 'register',
        title: 'Register',
        component: RegisterComponent,
        canActivate: [redirectAuthenticationGuard]
    }
];