import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IMedico } from 'app/shared/model/medico.model';
import { MedicoService } from './medico.service';
import { IEspecialidade } from 'app/shared/model/especialidade.model';
import { EspecialidadeService } from 'app/entities/especialidade';
import { IPlanoDeSaude } from 'app/shared/model/plano-de-saude.model';
import { PlanoDeSaudeService } from 'app/entities/plano-de-saude';

@Component({
    selector: 'jhi-medico-update',
    templateUrl: './medico-update.component.html'
})
export class MedicoUpdateComponent implements OnInit {
    private _medico: IMedico;
    isSaving: boolean;

    especialidades: IEspecialidade[];

    planodesaudes: IPlanoDeSaude[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private medicoService: MedicoService,
        private especialidadeService: EspecialidadeService,
        private planoDeSaudeService: PlanoDeSaudeService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ medico }) => {
            this.medico = medico;
        });
        this.especialidadeService.query().subscribe(
            (res: HttpResponse<IEspecialidade[]>) => {
                this.especialidades = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.planoDeSaudeService.query().subscribe(
            (res: HttpResponse<IPlanoDeSaude[]>) => {
                this.planodesaudes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.medico.id !== undefined) {
            this.subscribeToSaveResponse(this.medicoService.update(this.medico));
        } else {
            this.subscribeToSaveResponse(this.medicoService.create(this.medico));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IMedico>>) {
        result.subscribe((res: HttpResponse<IMedico>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackEspecialidadeById(index: number, item: IEspecialidade) {
        return item.id;
    }

    trackPlanoDeSaudeById(index: number, item: IPlanoDeSaude) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
    get medico() {
        return this._medico;
    }

    set medico(medico: IMedico) {
        this._medico = medico;
    }
}
