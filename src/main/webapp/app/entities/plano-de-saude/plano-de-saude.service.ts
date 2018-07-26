import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IPlanoDeSaude } from 'app/shared/model/plano-de-saude.model';

type EntityResponseType = HttpResponse<IPlanoDeSaude>;
type EntityArrayResponseType = HttpResponse<IPlanoDeSaude[]>;

@Injectable({ providedIn: 'root' })
export class PlanoDeSaudeService {
    private resourceUrl = SERVER_API_URL + 'api/plano-de-saudes';

    constructor(private http: HttpClient) {}

    create(planoDeSaude: IPlanoDeSaude): Observable<EntityResponseType> {
        return this.http.post<IPlanoDeSaude>(this.resourceUrl, planoDeSaude, { observe: 'response' });
    }

    update(planoDeSaude: IPlanoDeSaude): Observable<EntityResponseType> {
        return this.http.put<IPlanoDeSaude>(this.resourceUrl, planoDeSaude, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IPlanoDeSaude>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IPlanoDeSaude[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
