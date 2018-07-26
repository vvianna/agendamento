import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlanoDeSaude } from 'app/shared/model/plano-de-saude.model';

@Component({
    selector: 'jhi-plano-de-saude-detail',
    templateUrl: './plano-de-saude-detail.component.html'
})
export class PlanoDeSaudeDetailComponent implements OnInit {
    planoDeSaude: IPlanoDeSaude;

    constructor(private activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ planoDeSaude }) => {
            this.planoDeSaude = planoDeSaude;
        });
    }

    previousState() {
        window.history.back();
    }
}
