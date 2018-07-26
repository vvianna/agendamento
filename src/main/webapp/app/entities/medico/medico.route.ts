import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { Medico } from 'app/shared/model/medico.model';
import { MedicoService } from './medico.service';
import { MedicoComponent } from './medico.component';
import { MedicoDetailComponent } from './medico-detail.component';
import { MedicoUpdateComponent } from './medico-update.component';
import { MedicoDeletePopupComponent } from './medico-delete-dialog.component';
import { IMedico } from 'app/shared/model/medico.model';

@Injectable({ providedIn: 'root' })
export class MedicoResolve implements Resolve<IMedico> {
    constructor(private service: MedicoService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((medico: HttpResponse<Medico>) => medico.body));
        }
        return of(new Medico());
    }
}

export const medicoRoute: Routes = [
    {
        path: 'medico',
        component: MedicoComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'agendamentoApp.medico.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'medico/:id/view',
        component: MedicoDetailComponent,
        resolve: {
            medico: MedicoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agendamentoApp.medico.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'medico/new',
        component: MedicoUpdateComponent,
        resolve: {
            medico: MedicoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agendamentoApp.medico.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'medico/:id/edit',
        component: MedicoUpdateComponent,
        resolve: {
            medico: MedicoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agendamentoApp.medico.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const medicoPopupRoute: Routes = [
    {
        path: 'medico/:id/delete',
        component: MedicoDeletePopupComponent,
        resolve: {
            medico: MedicoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agendamentoApp.medico.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
