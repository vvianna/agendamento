import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { of } from 'rxjs';
import { map } from 'rxjs/operators';
import { PlanoDeSaude } from 'app/shared/model/plano-de-saude.model';
import { PlanoDeSaudeService } from './plano-de-saude.service';
import { PlanoDeSaudeComponent } from './plano-de-saude.component';
import { PlanoDeSaudeDetailComponent } from './plano-de-saude-detail.component';
import { PlanoDeSaudeUpdateComponent } from './plano-de-saude-update.component';
import { PlanoDeSaudeDeletePopupComponent } from './plano-de-saude-delete-dialog.component';
import { IPlanoDeSaude } from 'app/shared/model/plano-de-saude.model';

@Injectable({ providedIn: 'root' })
export class PlanoDeSaudeResolve implements Resolve<IPlanoDeSaude> {
    constructor(private service: PlanoDeSaudeService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(map((planoDeSaude: HttpResponse<PlanoDeSaude>) => planoDeSaude.body));
        }
        return of(new PlanoDeSaude());
    }
}

export const planoDeSaudeRoute: Routes = [
    {
        path: 'plano-de-saude',
        component: PlanoDeSaudeComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'agendamentoApp.planoDeSaude.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'plano-de-saude/:id/view',
        component: PlanoDeSaudeDetailComponent,
        resolve: {
            planoDeSaude: PlanoDeSaudeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agendamentoApp.planoDeSaude.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'plano-de-saude/new',
        component: PlanoDeSaudeUpdateComponent,
        resolve: {
            planoDeSaude: PlanoDeSaudeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agendamentoApp.planoDeSaude.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'plano-de-saude/:id/edit',
        component: PlanoDeSaudeUpdateComponent,
        resolve: {
            planoDeSaude: PlanoDeSaudeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agendamentoApp.planoDeSaude.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const planoDeSaudePopupRoute: Routes = [
    {
        path: 'plano-de-saude/:id/delete',
        component: PlanoDeSaudeDeletePopupComponent,
        resolve: {
            planoDeSaude: PlanoDeSaudeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'agendamentoApp.planoDeSaude.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
