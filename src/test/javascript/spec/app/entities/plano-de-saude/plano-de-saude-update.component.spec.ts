/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { AgendamentoTestModule } from '../../../test.module';
import { PlanoDeSaudeUpdateComponent } from 'app/entities/plano-de-saude/plano-de-saude-update.component';
import { PlanoDeSaudeService } from 'app/entities/plano-de-saude/plano-de-saude.service';
import { PlanoDeSaude } from 'app/shared/model/plano-de-saude.model';

describe('Component Tests', () => {
    describe('PlanoDeSaude Management Update Component', () => {
        let comp: PlanoDeSaudeUpdateComponent;
        let fixture: ComponentFixture<PlanoDeSaudeUpdateComponent>;
        let service: PlanoDeSaudeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AgendamentoTestModule],
                declarations: [PlanoDeSaudeUpdateComponent]
            })
                .overrideTemplate(PlanoDeSaudeUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(PlanoDeSaudeUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(PlanoDeSaudeService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new PlanoDeSaude(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.planoDeSaude = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new PlanoDeSaude();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.planoDeSaude = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
