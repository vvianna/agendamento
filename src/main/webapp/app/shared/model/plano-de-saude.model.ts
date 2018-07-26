import { IMedico } from 'app/shared/model//medico.model';
import { IEspecialidade } from 'app/shared/model//especialidade.model';

export interface IPlanoDeSaude {
    id?: number;
    nomePlano?: string;
    cnpj?: number;
    planoDeSaudes?: IMedico[];
    planoDeSaudes?: IEspecialidade[];
}

export class PlanoDeSaude implements IPlanoDeSaude {
    constructor(
        public id?: number,
        public nomePlano?: string,
        public cnpj?: number,
        public planoDeSaudes?: IMedico[],
        public planoDeSaudes?: IEspecialidade[]
    ) {}
}
