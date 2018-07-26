import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AgendamentoSharedModule } from 'app/shared';
import {
    EspecialidadeComponent,
    EspecialidadeDetailComponent,
    EspecialidadeUpdateComponent,
    EspecialidadeDeletePopupComponent,
    EspecialidadeDeleteDialogComponent,
    especialidadeRoute,
    especialidadePopupRoute
} from './';

const ENTITY_STATES = [...especialidadeRoute, ...especialidadePopupRoute];

@NgModule({
    imports: [AgendamentoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        EspecialidadeComponent,
        EspecialidadeDetailComponent,
        EspecialidadeUpdateComponent,
        EspecialidadeDeleteDialogComponent,
        EspecialidadeDeletePopupComponent
    ],
    entryComponents: [
        EspecialidadeComponent,
        EspecialidadeUpdateComponent,
        EspecialidadeDeleteDialogComponent,
        EspecialidadeDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgendamentoEspecialidadeModule {}
