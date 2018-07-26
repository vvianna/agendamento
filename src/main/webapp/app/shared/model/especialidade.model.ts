import { IMedico } from 'app/shared/model//medico.model';
import { IPlanoDeSaude } from 'app/shared/model//plano-de-saude.model';

export interface IEspecialidade {
    id?: number;
    nomeEspecialidade?: string;
    especialidades?: IMedico[];
    especialidades?: IPlanoDeSaude[];
}

export class Especialidade implements IEspecialidade {
    constructor(
        public id?: number,
        public nomeEspecialidade?: string,
        public especialidades?: IMedico[],
        public especialidades?: IPlanoDeSaude[]
    ) {}
}
