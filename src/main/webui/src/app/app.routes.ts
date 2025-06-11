import { Routes } from '@angular/router';
import { adminAuthGuard } from './guards/admin-auth.guard';
import { authorizationGuard } from './guards/authorization.guard';
import { redirectAuthenticationGuard } from './guards/redirect-authentication.guard';
import { teacherAuthGuard } from './guards/teacher-auth.guard';
import { AuditoriaComponent } from './pages/auditoria/auditoria.component';
import { CourseFormComponent } from './pages/course-form/course-form.component';
import { CourseHomeComponent } from './pages/course-home/course-home.component';
import { CourseSearchComponent } from './pages/course-search/course-search.component';
import { DashboardComponent } from './pages/dashboard/dashboard.component';
import { HomeComponent } from './pages/home/home.component';
import { LoginComponent } from './pages/login/login.component';
import { ProfileComponent } from './pages/profile/profile.component';
import { RegisterComponent } from './pages/register/register.component';
import { commonUserGuard } from './guards/common-user.guard';
import { RoleFormComponent } from './pages/role-form/role-form.component';
import { superAdminUserGuard } from './guards/super-admin-user.guard';
import { courseEditAuthGuard } from './guards/course-edit-auth.guard';
import { AvaliationListComponent } from './pages/avaliation-list/avaliation-list.component';

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
        path: 'roles',
        title: 'Permissões',
        component: RoleFormComponent,
        canActivate: [authorizationGuard, superAdminUserGuard]
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
        canActivate: [authorizationGuard, commonUserGuard]
    },
    {
        path: 'profile',
        title: 'Perfil',
        component: ProfileComponent,
        canActivate: [authorizationGuard]
    },
    {
        path: 'course',
        canActivate: [authorizationGuard],
        children: [
            {
                path: 'form',
                title: 'Criar curso',
                component: CourseFormComponent,
                canActivate: [teacherAuthGuard]
            },
            {
                path: 'form/:uuid',
                title: 'Editar curso',
                component: CourseFormComponent,
                canActivate: [courseEditAuthGuard]
            },
            {
                path: 'resume/:uuid',
                title: 'Curso',
                component: CourseHomeComponent,
                canActivate: [authorizationGuard],
            },
            {
                path: 'avaliation/:courseUUID',
                title: 'Avaliações',
                canActivate: [authorizationGuard],
                component: AvaliationListComponent
            }
        ]
    },
    {
        path: 'search/:search',
        title: 'Buscar Cursos',
        component: CourseSearchComponent,
        canActivate: [authorizationGuard]
    },
    {
        path: 'explore',
        title: 'Buscar Cursos',
        component: CourseSearchComponent,
        canActivate: [authorizationGuard]
    },
    {
        path:'audit',
        title: 'Auditoria',
        component: AuditoriaComponent,
        canActivate: [authorizationGuard, adminAuthGuard]
    }
];