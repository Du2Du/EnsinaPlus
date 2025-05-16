import { Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { LoginComponent } from './pages/login/login.component';

export const routes: Routes = [
    {
       path: '',
       title: 'Dashboard',
       component: DashboardComponent,
    },
    {
        path: 'login',
        title: 'Login',
        component: LoginComponent
    }
];