import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JhiAlertService } from 'ng-jhipster';

import { IPlanoDeSaude } from 'app/shared/model/plano-de-saude.model';
import { PlanoDeSaudeService } from './plano-de-saude.service';
import { IMedico } from 'app/shared/model/medico.model';
import { MedicoService } from 'app/entities/medico';
import { IEspecialidade } from 'app/shared/model/especialidade.model';
import { EspecialidadeService } from 'app/entities/especialidade';

@Component({
    selector: 'jhi-plano-de-saude-update',
    templateUrl: './plano-de-saude-update.component.html'
})
export class PlanoDeSaudeUpdateComponent implements OnInit {
    private _planoDeSaude: IPlanoDeSaude;
    isSaving: boolean;

    medicos: IMedico[];

    especialidades: IEspecialidade[];

    constructor(
        private jhiAlertService: JhiAlertService,
        private planoDeSaudeService: PlanoDeSaudeService,
        private medicoService: MedicoService,
        private especialidadeService: EspecialidadeService,
        private activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ planoDeSaude }) => {
            this.planoDeSaude = planoDeSaude;
        });
        this.medicoService.query().subscribe(
            (res: HttpResponse<IMedico[]>) => {
                this.medicos = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.especialidadeService.query().subscribe(
            (res: HttpResponse<IEspecialidade[]>) => {
                this.especialidades = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.planoDeSaude.id !== undefined) {
            this.subscribeToSaveResponse(this.planoDeSaudeService.update(this.planoDeSaude));
        } else {
            this.subscribeToSaveResponse(this.planoDeSaudeService.create(this.planoDeSaude));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<IPlanoDeSaude>>) {
        result.subscribe((res: HttpResponse<IPlanoDeSaude>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackMedicoById(index: number, item: IMedico) {
        return item.id;
    }

    trackEspecialidadeById(index: number, item: IEspecialidade) {
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
    get planoDeSaude() {
        return this._planoDeSaude;
    }

    set planoDeSaude(planoDeSaude: IPlanoDeSaude) {
        this._planoDeSaude = planoDeSaude;
    }
}
