import { Routes } from '@angular/router';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { LoginComponent } from './pages/login/login.component';
import { RegisterComponent } from './pages/register/register.component';
import { HomeComponent } from './pages/home/home.component';
import { authorizationGuard } from './guards/authorization.guard';
import { redirectAuthenticationGuard } from './guards/redirect-authentication.guard';
import { ProfileComponent } from './pages/profile/profile.component';
import { CourseFormComponent } from './pages/course-form/course-form.component';
import { teacherAuthGuard } from './guards/teacher-auth.guard';
import { AuditoriaComponent } from './pages/auditoria/auditoria.component';
import { adminAuthGuard } from './guards/admin-auth.guard';

export const routes: Routes = [
    {
        path: '',
        title: 'Dashboard',
        component: DashboardComponent,
        canActivate: [redirectAuthenticationGuard]
    },
    {
        path: 'login',
        title: 'Login',
        component: LoginComponent,
        canActivate: [redirectAuthenticationGuard]
    },
    {
        path: 'register',
        title: 'Registrar',
        component: RegisterComponent,
        canActivate: [redirectAuthenticationGuard]
    },
    {
        path: 'home',
        title: 'Home',
        component: HomeComponent,
        canActivate: [authorizationGuard]
    },
    {
        path: 'profile',
        title: 'Perfil',
        component: ProfileComponent,
        canActivate: [authorizationGuard]
    },
    {
        path: 'course/form',
        title: 'Criar curso',
        component: CourseFormComponent,
        canActivate: [authorizationGuard, teacherAuthGuard]
    },
    {
        path:'audit',
        title: 'Auditoria',
        component: AuditoriaComponent,
        canActivate: [authorizationGuard]
    }
];