/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AgendamentoTestModule } from '../../../test.module';
import { PlanoDeSaudeDetailComponent } from 'app/entities/plano-de-saude/plano-de-saude-detail.component';
import { PlanoDeSaude } from 'app/shared/model/plano-de-saude.model';

describe('Component Tests', () => {
    describe('PlanoDeSaude Management Detail Component', () => {
        let comp: PlanoDeSaudeDetailComponent;
        let fixture: ComponentFixture<PlanoDeSaudeDetailComponent>;
        const route = ({ data: of({ planoDeSaude: new PlanoDeSaude(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [AgendamentoTestModule],
                declarations: [PlanoDeSaudeDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(PlanoDeSaudeDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(PlanoDeSaudeDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.planoDeSaude).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
