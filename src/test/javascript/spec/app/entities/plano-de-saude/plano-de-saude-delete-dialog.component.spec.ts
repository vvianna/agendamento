/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { AgendamentoTestModule } from '../../../test.module';
import { PlanoDeSaudeDeleteDialogComponent } from 'app/entities/plano-de-saude/plano-de-saude-delete-dialog.component';
import { PlanoDeSaudeService } from 'app/entities/plano-de-saude/plano-de-saude.service';

describe('Component Tests', () => {
    describe('PlanoDeSaude Management Delete Component', () => {
        let comp: PlanoDeSaudeDeleteDialogComponent;
        let fixture: ComponentFixture<PlanoDeSaudeDeleteDialogComponent>;
        let service: PlanoDeSaudeService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AgendamentoTestModule],
                declarations: [PlanoDeSaudeDeleteDialogComponent]
            })
                .overrideTemplate(PlanoDeSaudeDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PlanoDeSaudeDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PlanoDeSaudeService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
