import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { AgendamentoPlanoDeSaudeModule } from './plano-de-saude/plano-de-saude.module';
import { AgendamentoEspecialidadeModule } from './especialidade/especialidade.module';
import { AgendamentoMedicoModule } from './medico/medico.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        AgendamentoPlanoDeSaudeModule,
        AgendamentoEspecialidadeModule,
        AgendamentoMedicoModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AgendamentoEntityModule {}
