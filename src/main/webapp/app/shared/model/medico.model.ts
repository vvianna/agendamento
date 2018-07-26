import { IEspecialidade } from 'app/shared/model//especialidade.model';
import { IPlanoDeSaude } from 'app/shared/model//plano-de-saude.model';

export interface IMedico {
    id?: number;
    nome?: string;
    crm?: string;
    medicos?: IEspecialidade[];
    medicos?: IPlanoDeSaude[];
}

export class Medico implements IMedico {
    constructor(
        public id?: number,
        public nome?: string,
        public crm?: string,
        public medicos?: IEspecialidade[],
        public medicos?: IPlanoDeSaude[]
    ) {}
}
