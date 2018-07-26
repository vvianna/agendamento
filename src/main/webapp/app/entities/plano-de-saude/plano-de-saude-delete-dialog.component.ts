import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPlanoDeSaude } from 'app/shared/model/plano-de-saude.model';
import { PlanoDeSaudeService } from './plano-de-saude.service';

@Component({
    selector: 'jhi-plano-de-saude-delete-dialog',
    templateUrl: './plano-de-saude-delete-dialog.component.html'
})
export class PlanoDeSaudeDeleteDialogComponent {
    planoDeSaude: IPlanoDeSaude;

    constructor(
        private planoDeSaudeService: PlanoDeSaudeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.planoDeSaudeService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'planoDeSaudeListModification',
                content: 'Deleted an planoDeSaude'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-plano-de-saude-delete-popup',
    template: ''
})
export class PlanoDeSaudeDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ planoDeSaude }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(PlanoDeSaudeDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.planoDeSaude = planoDeSaude;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
