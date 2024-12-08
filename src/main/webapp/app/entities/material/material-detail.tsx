import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './material.reducer';

export const MaterialDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const materialEntity = useAppSelector(state => state.material.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="materialDetailsHeading">
          <Translate contentKey="larissAsstApp.material.detail.title">Material</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{materialEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="larissAsstApp.material.name">Name</Translate>
            </span>
          </dt>
          <dd>{materialEntity.name}</dd>
          <dt>
            <span id="value">
              <Translate contentKey="larissAsstApp.material.value">Value</Translate>
            </span>
          </dt>
          <dd>{materialEntity.value}</dd>
        </dl>
        <Button tag={Link} to="/material" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/material/${materialEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MaterialDetail;
