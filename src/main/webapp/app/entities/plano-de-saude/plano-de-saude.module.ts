import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgendamentoSharedModule } from 'app/shared';
import {
    PlanoDeSaudeComponent,
    PlanoDeSaudeDetailComponent,
    PlanoDeSaudeUpdateComponent,
    PlanoDeSaudeDeletePopupComponent,
    PlanoDeSaudeDeleteDialogComponent,
    planoDeSaudeRoute,
    planoDeSaudePopupRoute
} from './';

const ENTITY_STATES = [...planoDeSaudeRoute, ...planoDeSaudePopupRoute];

@NgModule({
    imports: [AgendamentoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        PlanoDeSaudeComponent,
        PlanoDeSaudeDetailComponent,
        PlanoDeSaudeUpdateComponent,
        PlanoDeSaudeDeleteDialogComponent,
        PlanoDeSaudeDeletePopupComponent
    ],
    entryComponents: [
        PlanoDeSaudeComponent,
        PlanoDeSaudeUpdateComponent,
        PlanoDeSaudeDeleteDialogComponent,
        PlanoDeSaudeDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgendamentoPlanoDeSaudeModule {}
