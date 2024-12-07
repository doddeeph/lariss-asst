import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './case-size.reducer';

export const CaseSizeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const caseSizeEntity = useAppSelector(state => state.caseSize.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="caseSizeDetailsHeading">
          <Translate contentKey="larissAsstApp.caseSize.detail.title">CaseSize</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{caseSizeEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="larissAsstApp.caseSize.name">Name</Translate>
            </span>
          </dt>
          <dd>{caseSizeEntity.name}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="larissAsstApp.caseSize.value">Value</Translate>
            </span>
          </dt>
          <dd>{caseSizeEntity.value}</dd>
        </dl>
        <Button tag={Link} to="/case-size" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/case-size/${caseSizeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CaseSizeDetail;
