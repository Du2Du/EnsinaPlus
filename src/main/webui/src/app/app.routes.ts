import { Routes } from '@angular/router';
import { authorizationGuard } from './guards/authorization.guard';
import { redirectAuthenticationGuard } from './guards/redirect-authentication.guard';
import { studentAuthGuard } from './guards/student-auth.guard';
import { teacherAuthGuard } from './guards/teacher-auth.guard';
import { AuditoriaComponent } from './pages/auditoria/auditoria.component';
import { CourseFormComponent } from './pages/course-form/course-form.component';
import { CourseSearchComponent } from './pages/course-search/course-search.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { RegisterComponent } from './pages/register/register.component';
import { adminAuthGuard } from './guards/admin-auth.guard';
import { CourseHomeComponent } from './pages/course-home/course-home.component';

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
        path: 'course/:uuid',
        title: 'Curso',
        component: CourseHomeComponent,
        canActivate: [authorizationGuard]
    },
    {
        path: 'search/:search',
        title: 'Buscar Cursos',
        component: CourseSearchComponent,
        canActivate: [authorizationGuard],
        canDeactivate: [teacherAuthGuard]
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
        canActivate: [authorizationGuard, adminAuthGuard]
    }
];